package dev.jackson.dog_shelter_api.exception;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends RuntimeException{
    private final Issue issue;

    public InvalidDataException(String message) {
        super(message);
        this.issue = new Issue(message, HttpStatus.BAD_REQUEST);
    }
}
