package br.com.bancofeira.banco_feira.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EmpresaResponseDto {
    private Integer id;
    private String nomeFantasia;
    private String descricaoCurta;
    private BigDecimal creditos;
    private LocalDateTime dataCadastro;
    private Integer eventoId;
    private String chaveFuncionario;

    public static EmpresaResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Empresa empresa) {
        EmpresaResponseDto dto = new EmpresaResponseDto();
        dto.setId(empresa.getId());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setDescricaoCurta(empresa.getDescricaoCurta());
        dto.setCreditos(empresa.getCreditos());
        dto.setDataCadastro(empresa.getDataCadastro());
        if (empresa.getEvento() != null) {
             dto.setEventoId(empresa.getEvento().getId());
        }
        dto.setChaveFuncionario(empresa.getChaveFuncionario());
        return dto;
    }
}