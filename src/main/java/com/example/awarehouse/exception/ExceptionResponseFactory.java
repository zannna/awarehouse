package com.example.awarehouse.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ExceptionResponseFactory {

    private ExceptionResponseFactory() {}

    public static ResponseEntity<BasicErrorDto> basicErrorResponse(
        Exception e,
        HttpServletRequest request,
        HttpStatus responseStatus
    ) {
        BasicErrorDto error = new BasicErrorDto(
            Instant.now(),
            responseStatus.value(),
            responseStatus.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );

        return new ResponseEntity<>(error, responseStatus);
    }
}
