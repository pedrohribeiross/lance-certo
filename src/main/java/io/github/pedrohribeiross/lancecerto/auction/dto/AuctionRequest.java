package io.github.pedrohribeiross.lancecerto.auction.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record AuctionRequest(
        @NotBlank(message = "O título é obrigatório")
        String title,

        String description,

        @NotBlank(message = "O comitente é obrigatório")
        String principal,

        @NotNull(message = "A data de início é obrigatória")
        @FutureOrPresent(message = "A data de início não pode estar no passado")
        Instant startDate,

        @NotNull(message = "A data de término é obrigatória")
        @FutureOrPresent(message = "A data de término não pode estar no passado")
        Instant endDate
) {
    @AssertTrue(message = "A data de término deve ser posterior à de início")
    public boolean isEndDate() {
        if (startDate == null || endDate == null) return true;

        return endDate.isAfter(startDate);
    }
}

/*
  Por que Instant e não LocalDateTime?

  A diferença central: Instant é um ponto absoluto na linha do tempo (UTC); LocalDateTime é uma data-hora "solta", sem fuso.
  LocalDateTime é literalmente "2026-08-01 14:00" sem dizer 14:00 onde — é ambíguo por construção.

  Para leilão isso importa muito. Um leilão que encerra "às 14:00" precisa encerrar no mesmo instante físico para todos os licitantes,
  seja quem estiver em São Paulo, em Manaus (outro fuso) ou acessando de fora do país. Com Instant, você guarda o momento absoluto e cada cliente renderiza no fuso dele;
  a comparação "o lance chegou antes do fim?" é sempre não ambígua. Com LocalDateTime, você teria que carregar o fuso por fora e torcer para ninguém errar a
  conversão — e o horário de verão vira uma fonte de “bugs” sutis.

  Casa também com a coluna timestamptz do Postgres, que normaliza para UTC no armazenamento. LocalDateTime mapearia para timestamp (sem fuso),
  que é justamente o que você não quer aqui.

  Regra prática que vale carregar: momento no tempo → Instant (createdAt, start/end de leilão, timestamp de lance).
  LocalDate/LocalDateTime só quando o fuso é irrelevante por natureza (ex.: "data de nascimento", "feriado") — não é o caso de nada no núcleo do leilão.
 */