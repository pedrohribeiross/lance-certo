package br.com.leje.lancecerto.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AuctionRepository extends JpaRepository<Auction, UUID> {

    @Query("""
            SELECT a FROM Auction a
            WHERE (:status IS NULL OR a.status = :status)
            """)
    List<Auction> findAllByStatusFilter(@Param("status") AuctionStatus status);
}
