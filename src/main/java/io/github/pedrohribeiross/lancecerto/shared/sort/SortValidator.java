package io.github.pedrohribeiross.lancecerto.shared.sort;

import org.springframework.data.core.PropertyPath;
import org.springframework.data.domain.Sort;

public final class SortValidator {

    private SortValidator() {
    }

    public static void validate(Sort sort, Class<?> domainType) {
        sort.forEach(order ->
                // Lança PropertyReferenceException se a propriedade não existir na entidade
                PropertyPath.from(order.getProperty(), domainType)
        );
    }
}
