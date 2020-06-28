package com.example.mondayconnector.controllers;

import com.example.mondayconnector.models.Board;
import com.example.mondayconnector.models.BoardsResponse;
import com.example.mondayconnector.services.MondayService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
public class MondayController {

    @Autowired
    private MondayService mondayService;

    @ApiOperation(value = "Get list of all boards")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = BoardsResponse.class)
    })
    @GetMapping(value = "boards", produces = "application/json")
    public ResponseEntity<BoardsResponse> getBoards() {
        log.info("Received GET boards");
        List<Board> boards = mondayService.getAllBoards();
        log.info(boards.size() + " boards in memory");
        BoardsResponse response = new BoardsResponse(boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
