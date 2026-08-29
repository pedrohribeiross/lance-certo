package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionRequest;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionResponse;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionUpdateStatusRequest;
import io.github.pedrohribeiross.lancecerto.lot.LotRepository;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import io.github.pedrohribeiross.lancecerto.shared.exception.AuctionHasLotsException;
import io.github.pedrohribeiross.lancecerto.shared.exception.ResourceNotFoundException;
import io.github.pedrohribeiross.lancecerto.shared.sort.SortValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionMapper mapper;
    private final AuctionRepository repository;
    private final LotRepository lotRepository;

    public AuctionResponse create(AuctionRequest request) {
        Auction auction = mapper.toEntity(request);

        return mapper.toResponse(repository.save(auction));
    }

    public AuctionResponse findById(UUID id) {
        Auction auction = findByIdOrThrow(id);
        return mapper.toResponse(auction);
    }

    public PageResponse<AuctionResponse> findAll(AuctionStatus status, Pageable pageable) {
        SortValidator.validate(pageable.getSort(), Auction.class);
        Page<Auction> page = repository.findAllByStatusFilter(status, pageable);

        return mapper.toPageResponse(page);
    }

    @Transactional
    public AuctionResponse update(UUID id, AuctionRequest request) {
        Auction auction = findByIdOrThrow(id);
        mapper.updateEntity(request, auction);

        return mapper.toResponse(repository.save(auction));
    }

    @Transactional
    public AuctionResponse updateStatus(UUID id, AuctionUpdateStatusRequest request) {
        Auction auction = findByIdOrThrow(id);
        auction.transitionTo(request.status());

        return mapper.toResponse(repository.save(auction));
    }

    @Transactional
    public void delete(UUID id) {
        Auction auction = findByIdOrThrow(id);
        var hasLots = lotRepository.existsByAuctionId(auction.getId());

        if (hasLots) {
            throw new AuctionHasLotsException();
        }

        repository.delete(auction);
    }

    public Auction findByIdOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leilão"));
    }
}
