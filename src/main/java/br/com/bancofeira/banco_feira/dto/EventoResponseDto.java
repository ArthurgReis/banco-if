package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Evento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EventoResponseDto {
    private Integer id;
    private String nome;
    private LocalDate dataRealizacao;
    private BigDecimal creditoInicialCliente;
    private String chaveCliente;
    private String chaveEmpresa;

    public static EventoResponseDto fromEntity(Evento evento) {

        EventoResponseDto dto = new EventoResponseDto();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setDataRealizacao(evento.getDataRealizacao());
        dto.setCreditoInicialCliente(evento.getCreditoInicialCliente());
        dto.setChaveCliente(evento.getChaveCliente());
        dto.setChaveEmpresa(evento.getChaveEmpresa());
        return dto;
    }
}