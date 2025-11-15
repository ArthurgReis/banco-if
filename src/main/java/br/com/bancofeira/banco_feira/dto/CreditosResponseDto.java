package br.com.bancofeira.banco_feira.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreditosResponseDto {

    private BigDecimal creditos;
}