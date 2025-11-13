package br.com.bancofeira.banco_feira.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EventoRequestDto {

    @NotBlank(message = "O nome do evento é obrigatório.")
    private String nome;

    @NotNull(message = "A data de realização é obrigatória.")
    @FutureOrPresent(message = "A data do evento não pode ser no passado.")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dataRealizacao;

    @NotNull(message = "O crédito inicial é obrigatório.")
    @PositiveOrZero(message = "O crédito inicial não pode ser negativo.")
    private BigDecimal creditoInicialCliente;
}