package br.com.bancofeira.banco_feira.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class VendaSimulacaoResponseDto {
    private String cpfCliente;
    private BigDecimal saldoAtual;
    private BigDecimal totalCompra;
    private BigDecimal saldoFinalPrevisto;
    
    private List<String> alertas; 
    
    private boolean vendaAprovada; 
}