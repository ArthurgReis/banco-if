package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InscricaoEmpresaDto {
    
    @NotBlank(message = "A chave de inscrição é obrigatória.")
    private String chaveInscricao;

    @NotBlank(message = "O nome fantasia é obrigatório.")
    private String nomeFantasia;

    private String descricaoCurta;
}