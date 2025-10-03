package br.com.bancofeira.banco_feira.dto;
import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String senha;
}
