package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaCreateDto {

    @NotBlank(message = "A chave de inscrição da empresa é obrigatória.")
    private String chaveEmpresa;

    @NotBlank(message = "O nome fantasia é obrigatório.")
    private String nomeFantasia;

    private String descricaoCurta;
}