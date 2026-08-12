package br.com.leje.lancecerto.bid;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BidRepository extends JpaRepository<Bid, UUID> {

    boolean existsByLotId(UUID lotId);
}
