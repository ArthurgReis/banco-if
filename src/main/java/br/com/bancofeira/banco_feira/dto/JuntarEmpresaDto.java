package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JuntarEmpresaDto {
    @NotBlank(message = "A chave de funcionário é obrigatória.")
    private String chaveFuncionario;
}