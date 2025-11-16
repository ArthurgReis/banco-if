package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.StatusEmpresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminEmpresaDetalheDto {

    private Integer id;
    private Integer eventoId;
    private String nomeFantasia;
    private String descricaoCurta;
    private BigDecimal creditos;
    private StatusEmpresa status;
    private List<String> nomesIntegrantes;

    public static AdminEmpresaDetalheDto fromEntity(Empresa empresa) {
        AdminEmpresaDetalheDto dto = new AdminEmpresaDetalheDto();
        dto.setId(empresa.getId());
        
        if (empresa.getEvento() != null) {
            dto.setEventoId(empresa.getEvento().getId());
        }
        
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setDescricaoCurta(empresa.getDescricaoCurta());
        dto.setCreditos(empresa.getCreditos());
        dto.setStatus(empresa.getStatus());

        if (empresa.getFuncionarios() != null) {
            dto.setNomesIntegrantes(
                empresa.getFuncionarios().stream()
                    .map(Usuario::getNome) 
                    .collect(Collectors.toList())
            );
        } else {
            dto.setNomesIntegrantes(new ArrayList<>()); 
        }
        
        return dto;
    }
}