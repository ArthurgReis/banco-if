package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaUpdateDto {

    @NotBlank(message = "O nome fantasia é obrigatório.")
    private String nomeFantasia;

    private String descricaoCurta;
}