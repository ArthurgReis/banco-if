package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Pedido;
import br.com.bancofeira.banco_feira.model.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PedidoResponseDto {

    private Integer id;
    private Integer empresaId;
    private Integer clienteUsuarioId;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private LocalDateTime dataPedido;
    private List<ItemPedidoResponseDto> itens;

    public static PedidoResponseDto fromEntity(Pedido pedido) {
        PedidoResponseDto dto = new PedidoResponseDto();
        dto.setId(pedido.getId());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setStatus(pedido.getStatus());
        dto.setDataPedido(pedido.getDataPedido());

        if (pedido.getEmpresa() != null) {
            dto.setEmpresaId(pedido.getEmpresa().getId());
        }
        if (pedido.getInscricao() != null && pedido.getInscricao().getUsuario() != null) {
            dto.setClienteUsuarioId(pedido.getInscricao().getUsuario().getId());
        }

        if (pedido.getItens() != null) {
            dto.setItens(pedido.getItens().stream()
                    .map(ItemPedidoResponseDto::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}