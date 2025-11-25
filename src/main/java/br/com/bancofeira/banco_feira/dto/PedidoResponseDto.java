package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Pedido;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoResponseDto {
    private Integer id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private String nomeCliente;
    private String nomeEmpresa;
    private List<ItemPedidoResponseDto> itens;

    public static PedidoResponseDto fromEntity(Pedido pedido) {
        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setNomeCliente("Cliente CPF: " + pedido.getCliente().getCpf());
        dto.setNomeEmpresa(pedido.getEmpresa().getNomeFantasia());
        
        if(pedido.getItens() != null){
            dto.setItens(pedido.getItens().stream()
                .map(ItemPedidoResponseDto::fromEntity)
                .collect(Collectors.toList()));
        }
        return dto;
    }
}