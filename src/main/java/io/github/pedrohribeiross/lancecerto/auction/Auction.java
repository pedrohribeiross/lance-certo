package io.github.pedrohribeiross.lancecerto.auction;

import io.github.pedrohribeiross.lancecerto.shared.exception.InvalidStatusTransitionException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "auctions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "principal", nullable = false, length = 150)
    private String principal;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.NONE)
    @Column(name = "status", nullable = false)
    private AuctionStatus status = AuctionStatus.SCHEDULED;

    public void transitionTo(AuctionStatus target) {
        if(!this.status.canTransitionTo(target)) {
            throw new InvalidStatusTransitionException(this.status.name(), target.name());
        }

        this.status = target;
    }
}
