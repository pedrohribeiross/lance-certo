package io.github.pedrohribeiross.lancecerto.bid;

import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidRequest;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidResponse;
import io.github.pedrohribeiross.lancecerto.lot.Lot;
import io.github.pedrohribeiross.lancecerto.lot.LotService;
import io.github.pedrohribeiross.lancecerto.shared.exception.AuctionNotActiveException;
import io.github.pedrohribeiross.lancecerto.user.User;
import io.github.pedrohribeiross.lancecerto.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository repository;
    private final BidMapper mapper;
    private final LotService lotService;
    private final UserService userService;

    @Transactional
    public BidResponse create(UUID lotId, BidRequest request) {
        Lot lot = lotService.findByIdOrThrow(lotId);
        User user = userService.findByIdOrThrow(request.userId());

        if (!lot.getAuction().getStatus().equals(AuctionStatus.ACTIVE)) {
            throw new AuctionNotActiveException();
        }

        lot.placeBid(request.value());
        Bid bid = mapper.toEntity(request);
        bid.setUser(user);
        bid.setLot(lot);

        // TODO Fase 4: concorrência de lances simultâneos (lock otimista/pessimista)
        repository.save(bid);

        return mapper.toResponse(bid);
    }
}
