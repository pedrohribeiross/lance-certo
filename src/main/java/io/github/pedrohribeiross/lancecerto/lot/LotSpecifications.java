package io.github.pedrohribeiross.lancecerto.lot;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class LotSpecifications {
    public static Specification<Lot> categoryIdEqual(UUID categoryId) {
        return (root, query, builder) -> {
            if (categoryId != null) {
                return builder.equal(root.get("category").get("id"), categoryId);
            }

            return builder.conjunction();
        };
    }

    public static Specification<Lot> statusEqual(LotStatus status) {
        return (root, query, builder) -> {
            if (status != null) {
                return builder.equal(root.get("status"), status);
            }

            return builder.conjunction();
        };
    }

    public static Specification<Lot> currentValueBetween(BigDecimal minValue, BigDecimal maxValue) {
        return (root, query, builder) -> {
            if (minValue == null && maxValue == null) {
                return builder.conjunction();
            }

            if (maxValue == null) {
                return builder.greaterThanOrEqualTo(root.get("currentValue"), minValue);
            }

            if (minValue == null) {
                return builder.lessThanOrEqualTo(root.get("currentValue"), maxValue);
            }

            return builder.between(root.get("currentValue"), minValue, maxValue);
        };
    }

    public static Specification<Lot> keywordLike(String keyword) {
        return (root, query, builder) -> {
            if (keyword != null && !keyword.isBlank()) {
                return builder.like(builder.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
            }

            return builder.conjunction();
        };
    }
}
