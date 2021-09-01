package com.project.dogsear.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.charset.StandardCharsets;

@Data
@AllArgsConstructor
@Builder
public class DefaultRes<T> {

    private int statusCode;
    private String responseMessage;
    private T result;

    public DefaultRes(final int statusCode, final String responseMessage) {
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.result = null;
    }

    public static<T> DefaultRes<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage,null);
    }

    public static<T> DefaultRes<T> res(final int statusCode, final String responseMessage, final T t) {
        return DefaultRes.<T>builder()
                .result(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }
}