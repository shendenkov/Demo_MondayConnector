package com.example.mondayconnector.models.original;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardOriginal {

    private long id;
    private String name;
    private List<BoardColumn> columns;

    @JsonIgnore
    private List<ItemOriginal> items;

    @Override
    public String toString() {
        return "BoardOriginal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getStatusByIndex(int index) {
        Optional<BoardColumn> statusColumn = columns.stream()
                .filter(column -> column.getId().equalsIgnoreCase("status"))
                .findFirst();
        return statusColumn.map(column -> column.getLabels().get(index))
                .orElse("");
    }
}
