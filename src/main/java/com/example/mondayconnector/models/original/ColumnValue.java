package com.example.mondayconnector.models.original;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnValue {

    private String cid;
    private String name;
    private JsonNode value;

    @JsonIgnore
    private Map<String, Object> valueMap;

    public String getStatusIndex() {
        if (cid.equalsIgnoreCase("status")) {
            if (valueMap == null) {
                if (value instanceof NullNode) {
                    return "";
                }
                ObjectMapper mapper = new ObjectMapper();
                try {
                    valueMap = mapper.readValue(value.toString(), new TypeReference<Map<String, Object>>(){});
                } catch (JsonProcessingException e) {
                    log.error(e.getMessage());
                    return "";
                }
            }
            return valueMap.get("index").toString();
        } else {
            return "";
        }
    }
}
