package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.lot.dto.*;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LotController {

    private final LotService service;

    @PostMapping("auctions/{auctionId}/lots")
    public ResponseEntity<LotResponse> create(@PathVariable UUID auctionId, @RequestBody @Valid LotCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(auctionId, request));
    }

    @GetMapping("lots")
    public ResponseEntity<PageResponse<LotResponse>> findAll(
            @Valid @ModelAttribute LotFilterRequest filterRequest,
            @PageableDefault(size = 10, sort = "currentValue") Pageable pageable
    ) {
        return ResponseEntity.ok(service.findAll(filterRequest, pageable));
    }

    @GetMapping("lots/{id}")
    public ResponseEntity<LotResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("lots/{id}")
    public ResponseEntity<LotResponse> update(@PathVariable UUID id, @RequestBody @Valid LotUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("lots/{id}/status")
    public ResponseEntity<LotResponse> updateStatus(
            @PathVariable UUID id,
            @RequestBody LotUpdateStatusRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }

    @DeleteMapping("lots/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
