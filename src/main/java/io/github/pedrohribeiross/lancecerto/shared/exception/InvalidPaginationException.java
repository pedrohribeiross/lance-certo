package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class InvalidPaginationException extends DomainException {
    public InvalidPaginationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
