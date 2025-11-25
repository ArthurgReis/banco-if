package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

@Data
public class CheckoutRequestDto {

    @NotNull
    private Integer eventoId;

    @NotNull
    private Integer empresaId;

    @NotEmpty(message = "O carrinho não pode estar vazio.")
    @Valid
    private List<ItemCarrinhoDto> itens;

    @NotNull
    @CPF(message = "Digite um valor de CPF válido!")
    private String cpf;
}