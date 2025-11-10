package br.com.bancofeira.banco_feira.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UsuarioResponseDto {
    private Integer id;
    private String nome;
    private String email;
    private LocalDateTime dataCadastro;
    private Set<Integer> empresaIds;
    private Set<RoleDto> roles;

    public static UsuarioResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setDataCadastro(usuario.getDataCadastro());
        return dto;
    }
}