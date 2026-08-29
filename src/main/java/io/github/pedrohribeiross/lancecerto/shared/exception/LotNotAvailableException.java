package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class LotNotAvailableException extends DomainException {
    public LotNotAvailableException() {
        super(HttpStatus.CONFLICT, "Não é possível dar lances em um lote indisponível");
    }
}
