package com.alamu817group4.inventorypro.exceptions;

import com.alamu817group4.inventorypro.dtos.ResponseObject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.FetchNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * @author Oke
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InventoryProClientException.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(InventoryProClientException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler({InventoryProServerException.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(InventoryProServerException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> errorHandler(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage())
                .build());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> errorHandler(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        String messages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",\n"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(messages)
                .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("An error has occurred while processing your request, please try again")
                .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message("An error has occurred while processing your request, please try again")
                .build());
    }

    @ExceptionHandler({FetchNotFoundException.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(FetchNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<ResponseObject<Object>> errorHandler(UsernameNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                .status(ResponseObject.ResponseStatus.FAILED)
                .message(e.getMessage())
                .build());
    }

}
