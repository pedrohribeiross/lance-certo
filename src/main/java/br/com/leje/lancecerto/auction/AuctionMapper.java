package br.com.leje.lancecerto.auction;

import br.com.leje.lancecerto.auction.dto.AuctionRequest;
import br.com.leje.lancecerto.auction.dto.AuctionResponse;
import org.springframework.stereotype.Component;

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
        if (request != null) {
            if(request.title() != null) auction.setTitle(request.title());

            if(request.description() != null) auction.setDescription(request.description());

            if(request.principal() != null) auction.setPrincipal(request.principal());

            if(request.startDate() != null) auction.setStartDate(request.startDate());

            if(request.endDate() != null) auction.setEndDate(request.endDate());
        }
    }
}
