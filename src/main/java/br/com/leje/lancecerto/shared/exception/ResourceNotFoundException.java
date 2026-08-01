package br.com.leje.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " não encontrado.");
    }
}
