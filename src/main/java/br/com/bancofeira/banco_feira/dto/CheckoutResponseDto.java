package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Pedido;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CheckoutResponseDto {

    private PedidoResponseDto pedido;
    private BigDecimal novoSaldoCliente;

    public CheckoutResponseDto(Pedido pedido, BigDecimal novoSaldoCliente) {
        this.pedido = PedidoResponseDto.fromEntity(pedido);
        this.novoSaldoCliente = novoSaldoCliente;
    }
}