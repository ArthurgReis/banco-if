package br.com.bancofeira.banco_feira.dto;

import java.math.BigDecimal;
import java.util.Set;

import br.com.bancofeira.banco_feira.model.Role;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Integer id;
    private String nome;
    private String email;
    private String cpf;
    private BigDecimal creditos;
    private Set<Role> roles;
}
