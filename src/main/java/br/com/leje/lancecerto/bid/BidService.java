package br.com.leje.lancecerto.bid;

import br.com.leje.lancecerto.auction.AuctionStatus;
import br.com.leje.lancecerto.bid.dto.BidRequest;
import br.com.leje.lancecerto.bid.dto.BidResponse;
import br.com.leje.lancecerto.lot.Lot;
import br.com.leje.lancecerto.lot.LotService;
import br.com.leje.lancecerto.lot.LotStatus;
import br.com.leje.lancecerto.shared.exception.AuctionNotActiveException;
import br.com.leje.lancecerto.shared.exception.BidTooLowException;
import br.com.leje.lancecerto.shared.exception.LotNotAvailableException;
import br.com.leje.lancecerto.user.User;
import br.com.leje.lancecerto.user.UserService;
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

        if (!lot.getStatus().equals(LotStatus.AVAILABLE)) {
            throw new LotNotAvailableException();
        }

        if (request.value().compareTo(lot.getCurrentValue()) <= 0) {
            throw new BidTooLowException();
        }

        Bid bid = mapper.toEntity(request);
        lot.setCurrentValue(request.value());
        bid.setUser(user);
        bid.setLot(lot);

        // TODO Fase 4: concorrência de lances simultâneos (lock otimista/pessimista)
        repository.save(bid);

        return mapper.toResponse(bid);
    }
}
