package com.project.bookingApp.exceptions;

public class CompletionNotPossibleException extends RuntimeException{
    public CompletionNotPossibleException(String message)
    {
        super(message);
    }
}
