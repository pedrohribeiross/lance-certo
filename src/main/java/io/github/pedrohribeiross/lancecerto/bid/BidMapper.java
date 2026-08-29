package io.github.pedrohribeiross.lancecerto.bid;

import io.github.pedrohribeiross.lancecerto.bid.dto.BidRequest;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidResponse;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {

    public Bid toEntity(BidRequest request) {
        if (request == null) return null;

        Bid bid = new Bid();
        bid.setValue(request.value());

        return bid;
    }

    public BidResponse toResponse(Bid entity) {
        if (entity == null) return null;

        return new BidResponse(
                entity.getId(),
                entity.getLot().getId(),
                entity.getUser().getId(),
                entity.getValue(),
                entity.getCreatedAt()
        );
    }
}
