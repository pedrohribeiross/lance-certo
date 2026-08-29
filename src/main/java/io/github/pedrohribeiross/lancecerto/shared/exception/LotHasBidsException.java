package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class LotHasBidsException extends DomainException {
    public LotHasBidsException() {
        super(HttpStatus.CONFLICT, "Não é possível alterar ou remover um lote que já recebeu lances.");
    }
}
