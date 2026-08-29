package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

public class AuctionNotActiveException extends DomainException {
    public AuctionNotActiveException() {
        super(HttpStatus.CONFLICT, "Só é possível dar lances em leilões em andamento");
    }
}
