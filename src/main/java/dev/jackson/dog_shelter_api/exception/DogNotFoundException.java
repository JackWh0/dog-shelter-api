package dev.jackson.dog_shelter_api.exception;

import org.springframework.http.HttpStatus;

public class DogNotFoundException extends RuntimeException{
    private final Issue issue;

    public DogNotFoundException(String message){
        super(message);
        this.issue = new Issue(message, HttpStatus.NOT_FOUND);
    }
}
