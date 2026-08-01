package br.com.leje.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resourceName) {
        super(HttpStatus.NOT_FOUND, String.format("Recurso não encontrado: %s", resourceName));
    }
}
