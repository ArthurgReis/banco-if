package br.com.bancofeira.banco_feira.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean sucesso;
    private String mensagem;
    private T dados;
    
}
