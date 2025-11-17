package dev.jackson.dog_shelter_api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public Issue handleInvalidDataException(InvalidDataException e){
        return new Issue(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DogNotFoundException.class)
    public Issue DogNotFoundException(DogNotFoundException e){
        return new Issue(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Issue handleIllegalArgumentException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new Issue("Error in field: " + errors.toString(), HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Issue handleIllegalArgumentException(HttpMessageNotReadableException e){
        return new Issue("Invalid dog size. Use Uppercase - SM (for small), ME (for medium), LA (for large) or GI (for giant)", HttpStatus.BAD_REQUEST);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Issue handleIllegalArgumentException(DataIntegrityViolationException e){
        return new Issue("Repeated names are not allowed.", HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Issue handleException(Exception e) {
        return new Issue(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
