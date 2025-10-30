package br.com.bancofeira.banco_feira.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EsqueciSenhaDto {
    @Email(message = "O formato do e-mail é inválido.")
    private String email;
}
