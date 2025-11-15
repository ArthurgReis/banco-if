package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCarrinhoDto {
    @NotNull
    private Integer produtoId;

    @NotNull
    @Min(value = 1, message = "A quantidade deve ser de pelo menos 1.")
    private Integer quantidade;
}