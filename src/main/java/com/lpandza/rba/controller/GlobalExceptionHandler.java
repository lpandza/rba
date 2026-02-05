package com.lpandza.rba.controller;

import com.lpandza.rba.exception.ClientAlreadyExistsException;
import com.lpandza.rba.exception.ErrorResponse;
import com.lpandza.rba.restclient.cardapi.exception.CardApiException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClientExists(ClientAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(CardApiException.class)
    public ResponseEntity<ErrorResponse> handleCardApiException(CardApiException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> response = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> response.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleEnumValidationErrors(HttpMessageNotReadableException exception) {
        String errorDetails = "";

        if (exception.getCause() instanceof InvalidFormatException ifx) {
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                String fieldName = ifx.getPath().get(0).getPropertyName();
                Object[] enumValues = ifx.getTargetType().getEnumConstants();
                errorDetails = String.format(
                        "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                        ifx.getValue(), fieldName,
                        Arrays.toString(enumValues)
                );
            }
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorDetails));
    }
}
