package com.project.bookingApp.exceptions;

public class CancellationNotPossibleException extends RuntimeException{
    public CancellationNotPossibleException(String message)
    {
        super(message);
    }
}
