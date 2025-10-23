package br.com.bancofeira.banco_feira.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EventoResponseDto {
    private Integer id;
    private String nome;
    private LocalDate dataRealizacao;
    private BigDecimal creditoInicialCliente;

    public static EventoResponseDto fromEntity(br.com.bancofeira.banco_feira.model.Evento evento) {
        EventoResponseDto dto = new EventoResponseDto();
        dto.setId(evento.getId());
        dto.setNome(evento.getNome());
        dto.setDataRealizacao(evento.getDataRealizacao());
        dto.setCreditoInicialCliente(evento.getCreditoInicialCliente());
        return dto;
    }
}