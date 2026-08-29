package io.github.pedrohribeiross.lancecerto.auction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuctionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuctionRepository repository;

    @BeforeEach
    void setUp() {
        entityManager.persist(makeAuction("Veículos e motos", AuctionStatus.ACTIVE));
        entityManager.persist(makeAuction("Embarcações", AuctionStatus.SCHEDULED));
        entityManager.persist(makeAuction("Eletrônicos", AuctionStatus.SCHEDULED));
        entityManager.persist(makeAuction("Imóveis", AuctionStatus.CLOSED));
        entityManager.persist(makeAuction("Maquinas e equipamentos", AuctionStatus.CLOSED));
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should filter Auctions by status")
    void shouldFilterAuctionsByStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Auction> page = repository.findAllByStatusFilter(AuctionStatus.SCHEDULED, pageable);

        assertThat(page.getContent())
                .hasSize(2)
                .extracting(Auction::getTitle)
                .containsExactlyInAnyOrder("Embarcações", "Eletrônicos");
    }

    @Test
    @DisplayName("Should return paginated auctions")
    void shouldReturnPaginatedAuctions() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("status").ascending());
        Page<Auction> page = repository.findAllByStatusFilter(null, pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getContent())
                .extracting(Auction::getStatus)
                .isSorted();
    }

    @Test
    @DisplayName("Should return an empty list of auctions when there is no match")
    void shouldReturnEmptyAuctionsWhenThereIsNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Auction> page = repository.findAllByStatusFilter(AuctionStatus.CANCELLED, pageable);

        assertThat(page.getContent()).isEmpty();
    }

    private Auction makeAuction(String title, AuctionStatus status) {
        return new Auction(null, title, "mock description", "mock principal", Instant.now(), Instant.now(), status);
    }
}