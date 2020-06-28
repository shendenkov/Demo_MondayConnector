package com.example.mondayconnector.models.original;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOriginal {

    @JsonProperty("column_values")
    private List<ColumnValue> columnValues;

    @JsonIgnore
    private BoardOriginal parentBoard;

    @Override
    public String toString() {
        return "ItemOriginal{" +
                "name=" + getName() +
                ", status=" + getStatusName() +
                ", parentBoardID=" + parentBoard.getId() +
                '}';
    }

    public String getName() {
        Optional<ColumnValue> nameColumnValue = columnValues.stream()
                .filter(value -> value.getCid().equalsIgnoreCase("name"))
                .findFirst();
        return nameColumnValue.map(ColumnValue::getName)
                .orElse("");
    }

    public String getStatusName() {
        if (parentBoard == null) {
            return ""; // if item doesn't have board
        }
        Optional<ColumnValue> statusColumnValue = columnValues.stream()
                .filter(value -> value.getCid().equalsIgnoreCase("status"))
                .findFirst();
        if (statusColumnValue.isPresent()) {
            if (statusColumnValue.get().getValue() == null) {
                return ""; // if status object isn't set
            }
            String statusIndex = statusColumnValue.get().getStatusIndex();
            if (statusIndex == null || statusIndex.isEmpty()) {
                return ""; // if status index isn't set
            }
            try {
                return parentBoard.getStatusByIndex(Integer.parseInt(statusIndex));
            } catch (NumberFormatException ex) {
                return ""; // if status is set incorrectly
            }
        } else {
            return ""; // if item doesn't have status column
        }
    }
}
