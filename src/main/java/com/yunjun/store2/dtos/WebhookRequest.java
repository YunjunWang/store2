package com.yunjun.store2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebhookRequest {
    // the map will normalize all uppercase characters to lowercase characters
    private Map<String, String> headers;
    private String payload;
}
