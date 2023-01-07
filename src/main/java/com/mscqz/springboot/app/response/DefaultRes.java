package com.mscqz.springboot.app.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultRes<T> {

    private int statusCode;
    private String message;
    private T data;

    public DefaultRes(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> DefaultRes<T> res(final int statusCode, final String message) {
        return res(statusCode, message, null);
    }

    public static<T> DefaultRes<T> res(final int statusCode, final String message, final T t) {
        return DefaultRes.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .message(message)
                .build();
    }
}