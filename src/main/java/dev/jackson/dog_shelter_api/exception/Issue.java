package dev.jackson.dog_shelter_api.exception;

import org.springframework.http.HttpStatus;

public record Issue(String message, HttpStatus httpStatus) {
}
