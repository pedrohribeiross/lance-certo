package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionRequest;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionResponse;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuctionMapper {

    public Auction toEntity(AuctionRequest request) {
        if (request == null) return null;

        Auction auction = new Auction();
        auction.setTitle(request.title());
        auction.setDescription(request.description());
        auction.setPrincipal(request.principal());
        auction.setStartDate(request.startDate());
        auction.setEndDate(request.endDate());

        return auction;

    }

    public AuctionResponse toResponse(Auction auction) {
        if (auction == null) return null;

        return new AuctionResponse(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getPrincipal(),
                auction.getStartDate(),
                auction.getEndDate(),
                auction.getStatus()
        );
    }

    public void updateEntity(AuctionRequest request, Auction auction) {
        auction.setTitle(request.title());
        auction.setDescription(request.description());
        auction.setPrincipal(request.principal());
        auction.setStartDate(request.startDate());
        auction.setEndDate(request.endDate());
    }

    public PageResponse<AuctionResponse> toPageResponse(Page<Auction> page) {
        List<AuctionResponse> content = page.getContent().stream().map(this::toResponse).toList();
        return PageResponse.<AuctionResponse>builder()
                .content(content)
                .size(page.getSize())
                .page(page.getNumber())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
