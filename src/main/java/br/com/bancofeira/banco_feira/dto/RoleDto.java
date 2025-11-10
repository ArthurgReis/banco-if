package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Role;
import lombok.Data;

@Data
public class RoleDto {
    private Integer id;
    private String nome;

    // Método de conversão estático que faltava
    public static RoleDto fromEntity(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setNome(role.getNome());
        return dto;
    }
}