package br.com.leje.lancecerto.lot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LotRepository extends JpaRepository<Lot, UUID> {

    boolean existsByAuctionId(UUID auctionId);
}
