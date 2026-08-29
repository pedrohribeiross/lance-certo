package io.github.pedrohribeiross.lancecerto.bid;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.auction.AuctionStatus;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidRequest;
import io.github.pedrohribeiross.lancecerto.bid.dto.BidResponse;
import io.github.pedrohribeiross.lancecerto.category.Category;
import io.github.pedrohribeiross.lancecerto.lot.Lot;
import io.github.pedrohribeiross.lancecerto.lot.LotService;
import io.github.pedrohribeiross.lancecerto.lot.LotStatus;
import io.github.pedrohribeiross.lancecerto.shared.exception.AuctionNotActiveException;
import io.github.pedrohribeiross.lancecerto.shared.exception.BidTooLowException;
import io.github.pedrohribeiross.lancecerto.user.User;
import io.github.pedrohribeiross.lancecerto.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @Mock
    private BidRepository repository;
    @Mock
    private LotService lotService;
    @Mock
    private UserService userService;
    @Mock
    private BidMapper mapper;

    @InjectMocks
    private BidService service;

    @Nested
    class create {

        @Test
        @DisplayName("Should create a bid when the auction is active")
        void shouldCreateBidWhenAuctionIsActive() {
            // Arrange
            UUID lotId = UUID.randomUUID();
            UUID userId = UUID.randomUUID();

            Lot lot = makeLotWithAuction(AuctionStatus.ACTIVE);

            User user = new User();
            BidRequest request = new BidRequest(new BigDecimal("12000"), userId);
            Bid bid = new Bid();
            BidResponse expected = mock(BidResponse.class);

            when(lotService.findByIdOrThrow(lotId)).thenReturn(lot);
            when(userService.findByIdOrThrow(userId)).thenReturn(user);
            when(mapper.toEntity(request)).thenReturn(bid);
            when(mapper.toResponse(bid)).thenReturn(expected);

            // Act
            BidResponse result = service.create(lotId, request);

            // Assert
            assertThat(bid.getUser()).isEqualTo(user);
            assertThat(bid.getLot()).isEqualTo(lot);
            assertThat(result).isEqualTo(expected);
            verify(repository).save(bid);
        }

        @ParameterizedTest
        @DisplayName("Should reject a bid when the auction is not active")
        @EnumSource(value = AuctionStatus.class, names = "ACTIVE", mode = EnumSource.Mode.EXCLUDE)
        void shouldRejectBidWhenAuctionIsNotActive(AuctionStatus status) {
            // Arrange
            UUID lotId = UUID.randomUUID();
            Lot lot = makeLotWithAuction(status);
            BidRequest request = new BidRequest(new BigDecimal("12000"), UUID.randomUUID());

            when(lotService.findByIdOrThrow(lotId)).thenReturn(lot);

            // Act + Assert
            assertThatThrownBy(() -> service.create(lotId, request)).isInstanceOf(AuctionNotActiveException.class);

            // Assert
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw an exception when the auction is not active and the amount is invalid")
        void shouldThrowExceptionWhenAuctionIsNotActiveAndAmountIsInvalid() {
            // Arrange
            UUID lotId = UUID.randomUUID();
            Lot lot = makeLotWithAuction(AuctionStatus.CLOSED);

            BidRequest request = new BidRequest(new BigDecimal("0.01"), UUID.randomUUID());

            when(lotService.findByIdOrThrow(lotId)).thenReturn(lot);

            // Act + Assert
            assertThatThrownBy(() -> service.create(lotId, request))
                    .isInstanceOf(AuctionNotActiveException.class)
                    .isNotInstanceOf(BidTooLowException.class);

            verify(repository, never()).save(any());
        }

        private Lot makeLotWithAuction(AuctionStatus auctionStatus) {
            Auction auction = new Auction(null, "mock title", "mock description", "mock principal", Instant.now(), Instant.now(), auctionStatus);

            Category category = new Category();

            return new Lot(null, "mock description", new BigDecimal("10000"), new BigDecimal("10000"), new BigDecimal("100"), LotStatus.AVAILABLE, category, auction);
        }
    }
}