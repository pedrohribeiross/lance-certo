package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.support.fixtures.Auctions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AuctionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuctionRepository repository;

    @BeforeEach
    void setUp() {
        entityManager.persist(Auctions.with("Veículos e motos", AuctionStatus.ACTIVE));
        entityManager.persist(Auctions.with("Embarcações", AuctionStatus.SCHEDULED));
        entityManager.persist(Auctions.with("Eletrônicos", AuctionStatus.SCHEDULED));
        entityManager.persist(Auctions.with("Imóveis", AuctionStatus.CLOSED));
        entityManager.persist(Auctions.with("Maquinas e equipamentos", AuctionStatus.CLOSED));
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
}