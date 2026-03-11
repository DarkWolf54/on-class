package com.training.on_class.infrastructure.entrypoints.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String message;
    private T data;
}
