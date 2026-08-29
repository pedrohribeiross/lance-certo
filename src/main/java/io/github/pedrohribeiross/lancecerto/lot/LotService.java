package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionService;
import io.github.pedrohribeiross.lancecerto.bid.BidRepository;
import io.github.pedrohribeiross.lancecerto.category.Category;
import io.github.pedrohribeiross.lancecerto.category.CategoryService;
import io.github.pedrohribeiross.lancecerto.lot.dto.*;
import io.github.pedrohribeiross.lancecerto.lot.dto.*;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidFilterRangeException;
import io.github.pedrohribeiross.lancecerto.shared.exception.LotHasBidsException;
import io.github.pedrohribeiross.lancecerto.shared.exception.ResourceNotFoundException;
import io.github.pedrohribeiross.lancecerto.shared.sort.SortValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LotService {

    private final LotRepository repository;
    private final CategoryService categoryService;
    private final AuctionService auctionService;
    private final LotMapper mapper;
    private final BidRepository bidRepository;

    @Transactional
    public LotResponse create(UUID auctionId, LotCreateRequest request) {
        Auction auction = auctionService.findByIdOrThrow(auctionId);
        Category category = categoryService.findByIdOrThrow(request.categoryId());

        Lot lot = mapper.toEntity(request);
        lot.setCategory(category);
        lot.setAuction(auction);
        lot.setCurrentValue(request.startingBid());

        return mapper.toResponse(repository.save(lot));
    }

    @Transactional(readOnly = true)
    public LotResponse findById(UUID id) {
        Lot lot = findByIdOrThrow(id);
        return mapper.toResponse(lot);
    }

    @Transactional(readOnly = true)
    public PageResponse<LotResponse> findAll(LotFilterRequest filter, Pageable pageable) {
        if (filter.minValue() != null && filter.maxValue() != null
                && filter.minValue().compareTo(filter.maxValue()) > 0) {
            throw new InvalidFilterRangeException("O valor mínimo não pode ser maior que o máximo");
        }
        SortValidator.validate(pageable.getSort(), Lot.class);

        Specification<Lot> specs = Specification
                .where(LotSpecifications.categoryIdEqual(filter.category()))
                .and(LotSpecifications.statusEqual(filter.status()))
                .and(LotSpecifications.currentValueBetween(filter.minValue(), filter.maxValue()))
                .and(LotSpecifications.keywordLike(filter.search()));

        Page<Lot> results = repository.findAll(specs, pageable);

        return mapper.toPageResponse(results);
    }

    @Transactional
    public LotResponse update(UUID id, LotUpdateRequest request) {
        Lot lot = findByIdOrThrow(id);
        lotHasBids(id);

        Category category = categoryService.findByIdOrThrow(request.categoryId());

        lot.setDescription(request.description());
        lot.setCategory(category);

        return mapper.toResponse(repository.save(lot));
    }

    @Transactional
    public LotResponse updateStatus(UUID id, LotUpdateStatusRequest request) {
        Lot lot = findByIdOrThrow(id);
        lot.transitionTo(request.status());

        return mapper.toResponse(repository.save(lot));
    }

    @Transactional
    public void delete(UUID id) {
        Lot lot = findByIdOrThrow(id);
        lotHasBids(id);

        repository.delete(lot);
    }

    public Lot findByIdOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lote"));
    }

    private void lotHasBids(UUID lotId) {
        var hasBids = bidRepository.existsByLotId(lotId);

        if (hasBids) {
            throw new LotHasBidsException();
        }
    }
}
