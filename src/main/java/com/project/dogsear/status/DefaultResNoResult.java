package com.project.dogsear.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
@Builder
public class DefaultResNoResult<T> {

    private int statusCode;
    private String responseMessage;

    public DefaultResNoResult(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public static<T> DefaultResNoResult<T> res(final int statusCode, final String responseMessage) {
        return DefaultResNoResult.<T>builder()
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}
