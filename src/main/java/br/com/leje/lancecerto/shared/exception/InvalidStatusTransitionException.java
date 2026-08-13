package br.com.leje.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class InvalidStatusTransitionException extends DomainException{
    public InvalidStatusTransitionException(String statusFrom, String statusTo) {
        super(HttpStatus.CONFLICT, String.format("Transição inválida: de %s para %s não é permitida", statusFrom, statusTo));
    }
}
