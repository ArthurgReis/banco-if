package br.com.bancofeira.banco_feira.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.bancofeira.banco_feira.model.StatusEmpresa;
import lombok.Data;

@Data
public class EmpresaResponseDto {
    private Integer id;
    private String nomeFantasia;
    private String descricaoCurta;
    private BigDecimal creditos;
    private StatusEmpresa status;
    private LocalDateTime dataCadastro;
    private Integer eventoId;

    public static EmpresaResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Empresa empresa) {
        EmpresaResponseDto dto = new EmpresaResponseDto();
        dto.setId(empresa.getId());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setDescricaoCurta(empresa.getDescricaoCurta());
        dto.setCreditos(empresa.getCreditos());
        dto.setStatus(empresa.getStatus());
        dto.setDataCadastro(empresa.getDataCadastro());
        if (empresa.getEvento() != null) {
             dto.setEventoId(empresa.getEvento().getId());
        }
        return dto;
    }
}