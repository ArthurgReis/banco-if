package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class EmpresaRankingDto {

    private int id;
    private String nomeFantasia;
    private String nomeDono;
    private BigDecimal creditosAcumulados;

    public static EmpresaRankingDto fromEntity(Empresa empresa) {
        EmpresaRankingDto dto = new EmpresaRankingDto();
        dto.setId(empresa.getId());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setCreditosAcumulados(empresa.getCreditos());

        if (empresa.getFuncionarios() != null && !empresa.getFuncionarios().isEmpty()) {
            Usuario dono = empresa.getFuncionarios().iterator().next(); 
            dto.setNomeDono(dono.getNome());
        } else {
            dto.setNomeDono("Sem Dono Associado");
        }
        return dto;
    }
}