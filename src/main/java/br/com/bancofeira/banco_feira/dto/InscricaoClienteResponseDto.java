package br.com.bancofeira.banco_feira.dto;

import java.math.BigDecimal;

import br.com.bancofeira.banco_feira.model.Usuario;
import lombok.Data;

@Data
public class InscricaoClienteResponseDto {
    private Integer id;
    private Integer usuarioId;
    private String nomeCliente;
    private Integer eventoId;
    private BigDecimal creditos; 

    public static InscricaoClienteResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Inscricao inscricao) {
        InscricaoClienteResponseDto dto = new InscricaoClienteResponseDto();
        dto.setId(inscricao.getId());
        if (inscricao.getUsuario() != null) {
            dto.setUsuarioId(inscricao.getUsuario().getId());
        }
        Usuario cliente = inscricao.getUsuario();
        dto.setNomeCliente(cliente.getNome());
        if (inscricao.getEvento() != null) {
            dto.setEventoId(inscricao.getEvento().getId());
        }
        dto.setCreditos(inscricao.getCreditos());
        return dto;
    }
}
