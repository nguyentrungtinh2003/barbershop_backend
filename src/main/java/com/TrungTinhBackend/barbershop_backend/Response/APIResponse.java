package com.TrungTinhBackend.barbershop_backend.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class APIResponse {

    private Long statusCode;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
}
