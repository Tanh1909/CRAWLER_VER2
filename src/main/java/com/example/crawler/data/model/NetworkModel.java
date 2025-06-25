package com.example.crawler.data.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.openqa.selenium.devtools.v114.network.model.RequestId;

import java.util.Collections;
import java.util.Map;

@Data
@Accessors(chain = true)
public class NetworkModel {

    private RequestId requestId;

    private String url;

    private String method;

    private Map<String, Object> headers;

    private String requestBody;

    private String responseBody;

    private Integer statusCode;

    private Long timestamp;

    private boolean isComplete;

    private boolean isError;

    public Map<String, Object> getHeaders() {
        return headers == null ? Collections.emptyMap() : headers;
    }

    public String getRequestIdString() {
        return requestId != null ? requestId.toString() : null;
    }
}
