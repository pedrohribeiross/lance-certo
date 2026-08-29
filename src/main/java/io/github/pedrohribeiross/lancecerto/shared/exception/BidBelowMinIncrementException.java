package io.github.pedrohribeiross.lancecerto.shared.exception;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class BidBelowMinIncrementException extends DomainException {
    public BidBelowMinIncrementException(BigDecimal currentValue, BigDecimal minIncrement) {
        super(HttpStatus.UNPROCESSABLE_CONTENT, String.format(
                "O lance deve superar o valor atual (%s) em pelo menos %s, totalizando no mínimo %s",
                formatCurrency(currentValue),
                formatCurrency(minIncrement),
                formatCurrency(currentValue.add(minIncrement))
        ));
    }

    private static String formatCurrency(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            return "R$ 0,00";
        }

        Locale ptBr = Locale.forLanguageTag("pt-BR");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(ptBr);
        return numberFormat.format(value);
    }
}
