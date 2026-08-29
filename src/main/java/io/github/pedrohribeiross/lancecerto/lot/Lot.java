package io.github.pedrohribeiross.lancecerto.lot;

import io.github.pedrohribeiross.lancecerto.auction.Auction;
import io.github.pedrohribeiross.lancecerto.category.Category;
import io.github.pedrohribeiross.lancecerto.shared.exception.BidBelowMinIncrementException;
import io.github.pedrohribeiross.lancecerto.shared.exception.BidTooLowException;
import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidStatusTransitionException;
import io.github.pedrohribeiross.lancecerto.shared.exception.LotNotAvailableException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "lots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "starting_bid", nullable = false, scale = 2, precision = 15)
    private BigDecimal startingBid;

    @Column(name = "current_value", nullable = false, scale = 2, precision = 15)
    private BigDecimal currentValue;

    @Column(name = "min_increment", nullable = false, scale = 2, precision = 15)
    private BigDecimal minIncrement;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    @Column(name = "status", nullable = false)
    private LotStatus status = LotStatus.AVAILABLE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;

    public void transitionTo(LotStatus target) {
        if(!this.status.canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(this.status.name(), target.name());
        }

        this.status = target;
    }

    public void placeBid(BigDecimal value) {
        if (!this.status.equals(LotStatus.AVAILABLE)) {
            throw new LotNotAvailableException();
        }

        if (value.compareTo(this.currentValue) <= 0) {
            throw new BidTooLowException();
        }
        if (value.compareTo(this.currentValue.add(this.minIncrement)) < 0) {
            throw new BidBelowMinIncrementException(this.currentValue, this.minIncrement);
        }

        this.currentValue = value;
    }
}
