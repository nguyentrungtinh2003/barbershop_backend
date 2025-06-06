package com.TrungTinhBackend.barbershop_backend.Exception;

import com.TrungTinhBackend.barbershop_backend.Response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<APIResponse> handleNotFoundException(NotFoundException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(404L);
        response.setMessage("Error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<APIResponse> handleMailException(MailException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(500L);
        response.setMessage("Error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(400L);
        response.setMessage("Error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse> handleAccessDeniedException(AccessDeniedException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(403L);
        response.setMessage("Forbidden: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse> handleBadCredentialsException(BadCredentialsException ex) {
        APIResponse response = new APIResponse();
        response.setStatusCode(401L);
        response.setMessage("Invalid username or password !");
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Xử lý lỗi chung (Exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleGeneralException(Exception ex, WebRequest request) {

        if(request.getDescription(false).contains("/v3/api-docs")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        APIResponse response = new APIResponse();
        response.setStatusCode(500L);
        response.setMessage("System error: " + ex.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
