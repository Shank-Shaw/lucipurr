package com.lucipurr.tax.model;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ClientResponseVO<T> {
    private String desc;
    private T data;
    private HttpStatus status;

    public static <T> ClientResponseVOBuilder<T> ok() {
        ClientResponseVOBuilder<T> builder = getBuilder();
        builder.status(HttpStatus.OK);
        return builder;
    }

    public static <T> ClientResponseVOBuilder<T> created() {
        ClientResponseVOBuilder<T> builder = getBuilder();
        builder.status(HttpStatus.CREATED);
        return builder;
    }

    private static <T> ClientResponseVOBuilder<T> getBuilder() {
        return new ClientResponseVOBuilder<T>();
    }
}