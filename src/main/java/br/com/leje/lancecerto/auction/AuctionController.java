package br.com.leje.lancecerto.auction;

import br.com.leje.lancecerto.auction.dto.AuctionRequest;
import br.com.leje.lancecerto.auction.dto.AuctionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("{id}")
    public ResponseEntity<AuctionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }
}
