package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Role; 
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors; 

@Data
public class UsuarioResponseDto {
    private Integer id;
    private String nome;
    private String email;
    private String cpf; 
    private LocalDateTime dataCadastro;
    private Set<Integer> empresaIds; 
    private Set<RoleDto> roles;

    public static UsuarioResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpf(usuario.getCpf());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setEmpresaIds(usuario.getEmpresas().stream()
                                  .map(br.com.bancofeira.banco_feira.model.Empresa::getId)
                                  .collect(Collectors.toSet()));
        dto.setRoles(usuario.getRoles().stream()
                             .map(role -> {
                                 RoleDto roleDto = new RoleDto();
                                 roleDto.setId(role.getId());
                                 roleDto.setNome(role.getNome());
                                 return roleDto;
                             })
                             .collect(Collectors.toSet()));
        return dto;
    }
}