package br.com.bancofeira.banco_feira.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

        if (usuario.getEmpresas() != null) {
            dto.setEmpresaIds(usuario.getEmpresas().stream()
                    .map(br.com.bancofeira.banco_feira.model.Empresa::getId)
                    .collect(Collectors.toSet()));
        } else {
            dto.setEmpresaIds(new HashSet<>());
        }

        if (usuario.getRoles() != null) {
            dto.setRoles(usuario.getRoles().stream()
                    .map(RoleDto::fromEntity)
                    .collect(Collectors.toSet()));
        } else {
            dto.setRoles(new HashSet<>());
        }

        return dto;
    }
}