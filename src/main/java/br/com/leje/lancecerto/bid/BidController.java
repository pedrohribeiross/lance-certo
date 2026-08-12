package br.com.leje.lancecerto.bid;

import br.com.leje.lancecerto.bid.dto.BidRequest;
import br.com.leje.lancecerto.bid.dto.BidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService service;

    @PostMapping("/lots/{lotId}/bids")
    public ResponseEntity<BidResponse> create(@PathVariable("lotId") UUID lotId, @RequestBody BidRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(lotId, request));
    }
}
