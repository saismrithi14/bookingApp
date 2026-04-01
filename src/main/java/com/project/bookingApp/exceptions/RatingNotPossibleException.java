package com.project.bookingApp.exceptions;

public class RatingNotPossibleException extends RuntimeException{
    public RatingNotPossibleException(String message)
    {
        super(message);
    }
}
