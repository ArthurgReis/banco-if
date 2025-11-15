package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.ItemPedido;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemPedidoResponseDto {

    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;

    public static ItemPedidoResponseDto fromEntity(ItemPedido item) {
        ItemPedidoResponseDto dto = new ItemPedidoResponseDto();

        if (item.getProduto() != null) {
            dto.setNomeProduto(item.getProduto().getNome());
        }

        dto.setQuantidade(item.getQuantidade());
        dto.setValorUnitario(item.getValorUnitario());
        return dto;
    }
}