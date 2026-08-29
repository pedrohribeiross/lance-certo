package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.auction.dto.AuctionResponse;
import io.github.pedrohribeiross.lancecerto.shared.dto.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository repository;
    @Mock
    private AuctionMapper mapper;

    @InjectMocks
    private AuctionService service;

    @Nested
    class FindAll {

        @Test
        @DisplayName("Should query the auction repository when the sort is valid")
        void shouldQueryAuctionRepositoryWhenSortIsValid() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("startDate"));
            Page<Auction> page = Page.empty();
            PageResponse<AuctionResponse> pageResponse = new PageResponse<>();

            when(repository.findAllByStatusFilter(any(), eq(pageable))).thenReturn(page);
            when(mapper.toPageResponse(page)).thenReturn(pageResponse);

            PageResponse<AuctionResponse> result = service.findAll(AuctionStatus.ACTIVE, pageable);

            assertThat(result).isEqualTo(pageResponse);
            verify(repository).findAllByStatusFilter(AuctionStatus.ACTIVE, pageable);

        }

        @Test
        @DisplayName("Should throw an error without calling the repository when the sort is invalid")
        void shouldThrowErrorWithoutCallingRepositoryWhenSortIsInvalid() {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("fieldInexistent"));

            assertThatThrownBy(() -> service.findAll(AuctionStatus.ACTIVE, pageable)).isInstanceOf(PropertyReferenceException.class);

            verifyNoInteractions(repository);
        }
    }
}