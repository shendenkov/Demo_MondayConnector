package com.example.mondayconnector;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class MondaySource {

    @Value("${app.monday.url}")
    private String url;
    @Value("${app.monday.token}")
    private String token;
}
