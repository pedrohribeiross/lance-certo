package br.com.leje.lancecerto.lot;

import br.com.leje.lancecerto.auction.Auction;
import br.com.leje.lancecerto.auction.AuctionService;
import br.com.leje.lancecerto.bid.BidRepository;
import br.com.leje.lancecerto.category.Category;
import br.com.leje.lancecerto.category.CategoryService;
import br.com.leje.lancecerto.lot.dto.LotCreateRequest;
import br.com.leje.lancecerto.lot.dto.LotResponse;
import br.com.leje.lancecerto.lot.dto.LotUpdateRequest;
import br.com.leje.lancecerto.shared.exception.LotHasBidsException;
import br.com.leje.lancecerto.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
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
