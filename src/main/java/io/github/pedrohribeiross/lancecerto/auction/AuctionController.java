package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionRequest;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionResponse;
import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionUpdateStatusRequest;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final AuctionService service;

    @PostMapping
    public ResponseEntity<AuctionResponse> create(@RequestBody @Valid AuctionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<AuctionResponse>> getAll(
            @RequestParam(value = "status", required = false) AuctionStatus status,
            @PageableDefault(size = 10, sort = "startDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(status, pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<AuctionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<AuctionResponse> update(@PathVariable UUID id, @RequestBody @Valid AuctionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<AuctionResponse> updateStatus(@PathVariable UUID id, @RequestBody AuctionUpdateStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
