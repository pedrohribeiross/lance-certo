package br.com.leje.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class AuctionHasLotsException extends DomainException {
    public AuctionHasLotsException() {
        super(HttpStatus.CONFLICT, "Não é possível remover um leilão que possui lotes");
    }
}
