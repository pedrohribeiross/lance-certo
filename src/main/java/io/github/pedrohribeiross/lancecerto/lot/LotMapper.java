package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.category.dto.CategoryResponse;
import io.github.pedrohribeiross.lancecerto.lot.dto.LotCreateRequest;
import io.github.pedrohribeiross.lancecerto.lot.dto.LotResponse;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LotMapper {

    public Lot toEntity(LotCreateRequest request) {
        if (request == null) return null;

        Lot lot = new Lot();
        lot.setDescription(request.description());
        lot.setStartingBid(request.startingBid());
        lot.setMinIncrement(request.minIncrement());

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

    public PageResponse<LotResponse> toPageResponse(Page<Lot> page) {
        List<LotResponse> content = page.getContent().stream().map(this::toResponse).toList();
        return PageResponse.<LotResponse>builder()
                .content(content)
                .size(page.getSize())
                .page(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
