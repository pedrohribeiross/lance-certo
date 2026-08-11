package br.com.leje.lancecerto.lot;

import br.com.leje.lancecerto.category.dto.CategoryResponse;
import br.com.leje.lancecerto.lot.dto.LotCreateRequest;
import br.com.leje.lancecerto.lot.dto.LotResponse;
import org.springframework.stereotype.Component;

@Component
public class LotMapper {

    public Lot toEntity(LotCreateRequest request) {
        if (request == null) return null;

        Lot lot = new Lot();
        lot.setDescription(request.description());
        lot.setStartingBid(request.startingBid());

        return lot;
    }

    public LotResponse toResponse(Lot entity) {
        if (entity == null) return null;
        CategoryResponse category = new CategoryResponse(
                entity.getCategory().getId(),
                entity.getCategory().getName()
        );

        return new LotResponse(
                entity.getId(),
                entity.getAuction().getId(),
                entity.getDescription(),
                entity.getStartingBid(),
                entity.getCurrentValue(),
                entity.getStatus(),
                category
        );
    }
}
