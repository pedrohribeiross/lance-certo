package br.com.leje.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class BidTooLowException extends DomainException {
    public BidTooLowException() {
        super(HttpStatus.UNPROCESSABLE_CONTENT, "O lance deve ser maior que o valor atual do lote");
    }
}
