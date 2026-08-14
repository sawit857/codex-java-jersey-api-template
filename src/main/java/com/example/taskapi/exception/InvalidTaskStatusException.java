package com.example.taskapi.exception;

public class InvalidTaskStatusException extends RuntimeException {
    private final String value;

    public InvalidTaskStatusException(String value) {
        super("Task status is invalid");
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
