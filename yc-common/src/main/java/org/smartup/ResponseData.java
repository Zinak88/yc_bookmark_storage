package org.smartup;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
public class ResponseData {
    private int statusCode;
    private String body;
    private Map<String, String> headers = new HashMap<>();
    public ResponseData(String body) {
        this.body = body;
        statusCode = 200;
        headers.put("Content-Type", "application/json");
    }
    public ResponseData(int statusCode, String body) {
        this.body = body;
        this.statusCode = statusCode;
        headers.put("Content-Type", "application/json");
    }
    public ResponseData(int statusCode, String body, String contentType) {
        this.body = body;
        this.statusCode = statusCode;
        headers.put("Content-Type", contentType);
    }
}
