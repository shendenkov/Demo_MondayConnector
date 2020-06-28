package com.example.mondayconnector.services;

import com.example.mondayconnector.MondaySource;
import com.example.mondayconnector.models.Board;
import com.example.mondayconnector.models.Item;
import com.example.mondayconnector.models.original.BoardOriginal;
import com.example.mondayconnector.models.original.ItemOriginal;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MondayServiceImpl implements MondayService {

    private final List<Board> savedBoards = new ArrayList<>();

    @Autowired
    private MondaySource mondaySource;

    @Override
    public synchronized List<Board> getAllBoards() {
        return savedBoards;
    }

    @Scheduled(fixedDelay = 60000)
    public void heartbeat() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<BoardOriginal[]> boardsResponse = restTemplate.exchange(
                mondaySource.getUrl() + "/boards.json?api_key=" + mondaySource.getToken(),
                HttpMethod.GET, entity, BoardOriginal[].class);
        ResponseEntity<ItemOriginal[]> itemsResponse = null;

        log.info("Request boards status: " + boardsResponse.getStatusCode());
        if (boardsResponse.getStatusCode() == HttpStatus.OK) {
            log.info("Count: " + (boardsResponse.getBody() != null ? boardsResponse.getBody().length : 0) + " boards");
            if (boardsResponse.getBody() != null) {
                BoardOriginal[] boards = boardsResponse.getBody();
                for (BoardOriginal board : boards) {
                    itemsResponse = restTemplate.exchange(
                            mondaySource.getUrl() + "/boards/" + board.getId() + "/pulses.json?api_key=" + mondaySource.getToken(),
                            HttpMethod.GET, entity, ItemOriginal[].class);

                    log.info("Request board's items status: " + itemsResponse.getStatusCode());
                    if (itemsResponse.getStatusCode() == HttpStatus.OK) {
                        log.info("Count: " + (itemsResponse.getBody() != null ? itemsResponse.getBody().length : 0) + " columns");
                        if (itemsResponse.getBody() != null) {
                            ItemOriginal[] items = itemsResponse.getBody();
                            board.setItems(Arrays.asList(items));
                            Arrays.stream(items)
                                    .forEach(item -> item.setParentBoard(board));
                        }
                    }
                }
                if (itemsResponse != null && itemsResponse.getStatusCode() == HttpStatus.OK) {
                    synchronized (this) {
                        savedBoards.clear();
                        savedBoards.addAll(Arrays.stream(boards)
                                .map(boardOriginal -> {
                                    Board board = new Board();
                                    board.setName(boardOriginal.getName());
                                    board.setItems(boardOriginal.getItems().stream()
                                            .map(itemOriginal -> new Item(itemOriginal.getName(), itemOriginal.getStatusName()))
                                            .collect(Collectors.toList())
                                    );
                                    return board;
                                })
                                .collect(Collectors.toList())
                        );
                    }
                }
            }
        }
    }
}
