package com.example.awarehouse.module.token.exception;

import com.example.awarehouse.exception.BasicErrorDto;
import com.example.awarehouse.module.token.exception.exceptions.WarehouseNotHasSharingToken;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.awarehouse.exception.ExceptionResponseFactory.basicErrorResponse;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class SharingTokenExceptionHandler {

    @ExceptionHandler({
            WarehouseNotHasSharingToken.class
    })
    ResponseEntity<BasicErrorDto> sharingTokenNotFoundException(RuntimeException ex, HttpServletRequest request) {
        return basicErrorResponse(ex, request, NOT_FOUND);
    }

}