package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.lot.dto.LotFilterRequest;
import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidFilterRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;;

@ExtendWith(MockitoExtension.class)
class LotServiceTest {

    @Mock
    private LotRepository repository;

    @InjectMocks
    private LotService service;

    @Nested
    class FindAll {
        @Test
        @DisplayName("Should reject finding all lots when the minimum value is greater than max value")
        void shouldRejectFindingAllLotsWhenMinGreaterThanMax() {
            LotFilterRequest filter = new LotFilterRequest(null, null, new BigDecimal("1000"), new BigDecimal("500"), null);
            Pageable pageable = PageRequest.of(0, 2, Sort.by("currentValue").ascending());

            assertThatThrownBy(() -> service.findAll(filter, pageable)).isInstanceOf(InvalidFilterRangeException.class);

            // ArgumentMatchers -> Aqui você diz explicitamente ao compilador qual o tipo genérico, então não há inferência incompleta nem warning.
            verify(repository, never()).findAll(ArgumentMatchers.<Specification<Lot>>any(), any(Pageable.class));
        }
    }
}