package io.github.pedrohribeiross.lancecerto.lot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface LotRepository extends JpaRepository<Lot, UUID>, JpaSpecificationExecutor<Lot> {

    boolean existsByAuctionId(UUID auctionId);
}
