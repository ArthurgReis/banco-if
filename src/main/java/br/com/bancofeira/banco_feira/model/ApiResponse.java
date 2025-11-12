package br.com.bancofeira.banco_feira.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean sucesso;
    private String mensagem;
    private T dados;
    private String token;

    public ApiResponse(boolean sucesso, String mensagem, T dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
        this.token = null;
    }

    public ApiResponse(boolean sucesso, String mensagem, T dados, String token) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
        this.token = token;
    }
    
}
