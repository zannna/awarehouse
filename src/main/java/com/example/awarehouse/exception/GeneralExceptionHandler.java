package com.example.awarehouse.exception;

import com.example.awarehouse.exception.exceptions.UserUnauthorized;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

import static com.example.awarehouse.exception.ExceptionResponseFactory.basicErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class,
            InvalidFormatException.class, MismatchedInputException.class, HttpMessageNotReadableException.class})
    ResponseEntity<BasicErrorDto> generalBadRequests(Exception e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return basicErrorResponse(e, request, BAD_REQUEST, "Invalid data. Try again ");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    ResponseEntity<BasicErrorDto> methodArgumentTypeBadRequest(Exception e, HttpServletRequest request) {
        MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
        String message = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(", "));

        BasicErrorDto error = new BasicErrorDto(Instant.now(), BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase(), message, request.getRequestURI());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }


    @ExceptionHandler({UserUnauthorized.class,})
    ResponseEntity<BasicErrorDto> generalUnauthorized(Exception e, HttpServletRequest request) {
        return basicErrorResponse(e, request, UNAUTHORIZED);
    }


}
