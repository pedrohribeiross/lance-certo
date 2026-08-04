package br.com.leje.lancecerto.auction;

import br.com.leje.lancecerto.auction.dto.AuctionRequest;
import br.com.leje.lancecerto.auction.dto.AuctionResponse;
import br.com.leje.lancecerto.auction.dto.AuctionUpdateStatusRequest;
import br.com.leje.lancecerto.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionMapper mapper;
    private final AuctionRepository repository;

    public AuctionResponse create(AuctionRequest request) {
        Auction auction = mapper.toEntity(request);

        return mapper.toResponse(repository.save(auction));
    }

    public AuctionResponse findById(UUID id) {
        Auction auction = findByIdOrThrow(id);
        return mapper.toResponse(auction);
    }

    public List<AuctionResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
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
        auction.setStatus(request.status());

        return mapper.toResponse(repository.save(auction));
    }

    @Transactional
    public void delete(UUID id) {
        Auction auction = findByIdOrThrow(id);
        // TODO Fase Lot: bloquear 409 se houver lotes vinculados

        repository.delete(auction);
    }

    public Auction findByIdOrThrow(UUID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Leilão"));
    }
}
