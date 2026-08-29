package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;
import io.github.pedrohribeiross.lancecerto.category.Category;
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
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LotSpecificationsTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LotRepository repository;

    private Category categoryOther;
    private Auction auction;

    @BeforeEach
    void setUp() {
        Category categoryVehicle = new Category();
        categoryVehicle.setName("Vehicle");
        entityManager.persist(categoryVehicle);

        categoryOther = new Category();
        categoryOther.setName("Other");
        entityManager.persist(categoryOther);

        auction = new Auction(null, "mock title", "mock description", "mock principal", Instant.now(), Instant.now(), AuctionStatus.ACTIVE);
        entityManager.persist(auction);

        entityManager.persist(makeLot("Notebook Dell", new BigDecimal("15000"), LotStatus.AVAILABLE, categoryOther));
        entityManager.persist(makeLot("Notebook Asus", new BigDecimal("10000"), LotStatus.AWARDED, categoryOther));
        entityManager.persist(makeLot("Phone", new BigDecimal("7000"), LotStatus.SUSPENDED, categoryOther));
        entityManager.persist(makeLot("Vehicle", new BigDecimal("12000"), LotStatus.AVAILABLE, categoryVehicle));

        entityManager.flush();

        // Info didático: adicionamos `entityManager.clear();` para que ele esvazie a memória do insert para que
        // rode o SELECT com a Specification buscando do banco zerado.
        entityManager.clear();
    }

    @Test
    @DisplayName("Should filter lots by category")
    void shouldFilterByCategory() {
        Specification<Lot> spec = LotSpecifications.categoryIdEqual(categoryOther.getId());
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(3)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Notebook Dell", "Notebook Asus", "Phone");
    }

    @Test
    @DisplayName("Should filter lots by status")
    void shouldFilterByStatus() {
        Specification<Lot> spec = LotSpecifications.statusEqual(LotStatus.SUSPENDED);
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Phone");
    }

    @Test
    @DisplayName("Should filter lots by minimum value only")
    void shouldFilterByMinValueOnly() {
        Specification<Lot> spec = LotSpecifications.currentValueBetween(new BigDecimal("11000"), null);
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(2)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Notebook Dell", "Vehicle");
    }

    @Test
    @DisplayName("Should filter lots by max value only")
    void shouldFilterByMaxValueOnly() {
        Specification<Lot> spec = LotSpecifications.currentValueBetween(null, new BigDecimal("9000"));
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Phone");
    }

    @Test
    @DisplayName("Should filter lots by value range")
    void shouldFilterByValueRange() {
        Specification<Lot> spec = LotSpecifications.currentValueBetween(new BigDecimal("9000"), new BigDecimal("11000"));
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Notebook Asus");
    }

    @Test
    @DisplayName("Should filter lots by keyword case-insensitive")
    void shouldFilterByKeywordCaseInsensitive() {
        Specification<Lot> spec = LotSpecifications.keywordLike("NOTEBOOK");
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(2)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Notebook Dell", "Notebook Asus");
    }

    @Test
    @DisplayName("Should filter lots when all filters are combined")
    void shouldFilterWhenAllFiltersAreCombined() {
        Specification<Lot> spec = Specification
                .where(LotSpecifications.categoryIdEqual(categoryOther.getId()))
                .and(LotSpecifications.statusEqual(LotStatus.AVAILABLE))
                .and(LotSpecifications.currentValueBetween(new BigDecimal("10000"), new BigDecimal("15000")))
                .and(LotSpecifications.keywordLike("notebook"));
        List<Lot> result = repository.findAll(spec);

        assertThat(result)
                .hasSize(1)
                .extracting(Lot::getDescription)
                .containsExactly("Notebook Dell");
    }

    @Test
    @DisplayName("Should return all lots when no filter is provided")
    void shouldReturnAllLotsWhenNoFilterIsProvided() {
        Specification<Lot> spec = Specification.unrestricted();
        List<Lot> page = repository.findAll(spec);

        assertThat(page)
                .hasSize(4)
                .extracting(Lot::getDescription)
                .containsExactlyInAnyOrder("Notebook Dell", "Notebook Asus", "Phone", "Vehicle");
    }

    @Test
    @DisplayName("Should return an empty list of lots when there is no match")
    void shouldReturnEmptyLotsWhenThereIsNoMatch() {
        Specification<Lot> spec = LotSpecifications.keywordLike("non-existent");
        List<Lot> page = repository.findAll(spec);

        assertThat(page).isEmpty();
    }

    @Test
    @DisplayName("Should return paginated lots")
    void shouldReturnPaginatedLots() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("currentValue").ascending());
        Specification<Lot> spec = Specification.unrestricted();
        Page<Lot> page = repository.findAll(spec, pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(4);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(Lot::getCurrentValue)
                .isSorted();
    }

    private Lot makeLot(String description, BigDecimal value, LotStatus status, Category category) {
        return new Lot(null, description, value, value, new BigDecimal("1"), status, category, auction);
    }
}