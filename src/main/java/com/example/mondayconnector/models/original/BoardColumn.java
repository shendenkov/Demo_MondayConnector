package com.example.mondayconnector.models.original;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardColumn {

    private String id;
    private Map<Integer, String> labels;
}
