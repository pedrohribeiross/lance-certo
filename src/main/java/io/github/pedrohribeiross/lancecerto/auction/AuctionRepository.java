package io.github.pedrohribeiross.lancecerto.auction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {

    @Query("""
            SELECT a FROM Auction a
            WHERE (:status IS NULL OR a.status = :status)
            """)
    Page<Auction> findAllByStatusFilter(@Param("status") AuctionStatus status, Pageable pageable);
}
