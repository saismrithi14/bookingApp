package com.project.bookingApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<String> noDriverFound(DriverNotFoundException de)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(de.getMessage());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> parseError(HttpMessageNotReadableException he)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred while parsing the input");
    }
    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<String> rideNotFound(RideNotFoundException re)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re.getMessage());
    }

    @ExceptionHandler(CancellationNotPossibleException.class)
    public ResponseEntity<String> cancelNotPossible(CancellationNotPossibleException ce)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ce.getMessage());
    }
    @ExceptionHandler(CompletionNotPossibleException.class)
    public ResponseEntity<String> completeNotPossible(CompletionNotPossibleException ce)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ce.getMessage());
    }
}
