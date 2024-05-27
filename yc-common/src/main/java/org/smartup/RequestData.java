package org.smartup;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RequestData {
    private String body;
    private String httpMethod;
    private String path;
    private Map<String, String> queryStringParameters;
    private String url;
    private Map<String, String> pathParams;
    private Map<String, String> headers;
}
