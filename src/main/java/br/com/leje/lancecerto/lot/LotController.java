package br.com.leje.lancecerto.lot;

import br.com.leje.lancecerto.lot.dto.LotCreateRequest;
import br.com.leje.lancecerto.lot.dto.LotResponse;
import br.com.leje.lancecerto.lot.dto.LotUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LotController {

    private final LotService service;

    @PostMapping("auctions/{id}/lots")
    public ResponseEntity<LotResponse> create(@PathVariable("id") UUID auctionId, @RequestBody @Valid LotCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(auctionId, request));
    }

    @GetMapping("lots/{id}")
    public ResponseEntity<LotResponse> findById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("lots/{id}")
    public ResponseEntity<LotResponse> update(@PathVariable("id") UUID id, @RequestBody @Valid LotUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("lots/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") UUID id) {
        service.delete(id);
    }
}
