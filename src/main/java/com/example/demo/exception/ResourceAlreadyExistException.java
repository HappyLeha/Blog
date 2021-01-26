package com.example.demo.exception;

public class ResourceAlreadyExistException extends RuntimeException {

    @Override
    public String getMessage() {
        return "User with this email already exist";
    }
}
