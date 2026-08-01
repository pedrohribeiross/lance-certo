package br.com.leje.lancecerto.shared.dto;

public record ValidationError(
        String field,
        String message
) {
}
