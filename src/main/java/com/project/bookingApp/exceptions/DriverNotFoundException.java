package com.project.bookingApp.exceptions;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(String message)
    {
        super(message);
    }
}
