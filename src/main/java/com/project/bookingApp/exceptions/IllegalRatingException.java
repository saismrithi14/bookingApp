package com.project.bookingApp.exceptions;

public class IllegalRatingException extends RuntimeException{
    public IllegalRatingException(String message)
    {
        super(message);
    }
}
