package br.com.bancofeira.banco_feira.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProdutoResponseDto {
    private Integer id;
    private Integer empresaId;
    private String nome;
    private BigDecimal valor;
    private Integer quantidadeEstoque;

    public static ProdutoResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Produto produto) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        if (produto.getEmpresa() != null) {
            dto.setEmpresaId(produto.getEmpresa().getId());
        }
        dto.setNome(produto.getNome());
        dto.setValor(produto.getValor());
        dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        return dto;
    }
}