package com.example.awarehouse.module.warehouse.util.exception;

import com.example.awarehouse.exception.BasicErrorDto;
import com.example.awarehouse.module.warehouse.util.exception.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.awarehouse.exception.ExceptionResponseFactory.basicErrorResponse;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class WarehouseExceptionHandler {
    @ExceptionHandler({GroupDuplicateException.class,})
    ResponseEntity<BasicErrorDto> warehouseBadRequestException(Exception e, HttpServletRequest request) {
        return basicErrorResponse(e, request, BAD_REQUEST);
    }

    @ExceptionHandler({ShelveNotExist.class, WarehouseNotExistException.class,
    WorkerWarehouseRelationNotExist.class, GroupNotExistException.class})
    ResponseEntity<BasicErrorDto> warehouseNotFoundException(Exception e, HttpServletRequest request) {
        return basicErrorResponse(e, request, NOT_FOUND);
    }
}
