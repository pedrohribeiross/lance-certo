package br.com.leje.lancecerto.lot;

import br.com.leje.lancecerto.auction.Auction;
import br.com.leje.lancecerto.category.Category;
import br.com.leje.lancecerto.shared.exception.InvalidStatusTransitionException;
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

    @Column(name = "starting_bid", nullable = false)
    private BigDecimal startingBid;

    @Column(name = "current_value", nullable = false)
    private BigDecimal currentValue;

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
}
