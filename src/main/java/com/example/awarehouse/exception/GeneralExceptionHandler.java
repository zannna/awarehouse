package com.example.awarehouse.exception;

import com.example.awarehouse.exception.exceptions.UserUnauthorized;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.example.awarehouse.exception.ExceptionResponseFactory.basicErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@ControllerAdvice
public
class GeneralExceptionHandler {

    @ExceptionHandler(
        {
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
                MethodArgumentNotValidException.class
        }
    )
    ResponseEntity<BasicErrorDto> generalBadRequests(Exception e, HttpServletRequest request) {
        return basicErrorResponse(e, request, BAD_REQUEST);
    }

    @ExceptionHandler(
            {
                    UserUnauthorized.class,
            }
    )
    ResponseEntity<BasicErrorDto> generalUnauthorized(Exception e, HttpServletRequest request) {
        return basicErrorResponse(e, request, UNAUTHORIZED);
    }


}
