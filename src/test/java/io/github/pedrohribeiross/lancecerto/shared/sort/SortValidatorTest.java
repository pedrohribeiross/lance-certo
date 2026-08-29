package io.github.pedrohribeiross.lancecerto.shared.sort;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SortValidatorTest {

    @Test
    @DisplayName("Should accept a valid entity field")
    void shouldAcceptValidEntityField() {
        Sort sort = Sort.by("status");
        assertThatNoException().isThrownBy(() -> SortValidator.validate(sort, Auction.class));
    }

    @Test
    @DisplayName("Should accept multiple valid entity fields")
    void shouldAcceptMultipleValidEntityFields() {
        Sort sort = Sort.by("status").and(Sort.by("description"));
        assertThatNoException().isThrownBy(() -> SortValidator.validate(sort, Auction.class));
    }

    @Test
    @DisplayName("Should throw an error when the entity field does not exist")
    void shouldThrowErrorWhenEntityFieldDoesNotExist() {
        Sort sort = Sort.by("fieldInexistent");
        assertThatThrownBy(() -> SortValidator.validate(sort, Auction.class)).isInstanceOf(PropertyReferenceException.class);
    }

    @Test
    @DisplayName("Should throw an error when the first invalid field is found among multiple sorts")
    void shouldThrowErrorWhenFirstInvalidFieldIsFoundAmongMultipleSorts() {
        Sort sort = Sort.by("status").and(Sort.by("fieldInexistent"));
        assertThatThrownBy(() -> SortValidator.validate(sort, Auction.class)).isInstanceOf(PropertyReferenceException.class);
    }

    @Test
    @DisplayName("Should not throw an error when the sort is empty")
    void shouldNotThrowErrorWhenSortIsEmpty() {
        assertThatNoException().isThrownBy(() -> SortValidator.validate(Sort.unsorted(), Auction.class));
    }
}