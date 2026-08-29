package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class InvalidFilterRangeException extends DomainException{
    public InvalidFilterRangeException(String message) {
        super(HttpStatus.UNPROCESSABLE_CONTENT, message);
    }
}
