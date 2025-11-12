package br.com.bancofeira.banco_feira.dto;

import br.com.bancofeira.banco_feira.model.Evento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EventoPublicDto {

    private String nome;
    private LocalDate dataRealizacao;
    private BigDecimal creditoInicialCliente;

    public static EventoPublicDto fromEntity(Evento evento) {
        EventoPublicDto dto = new EventoPublicDto();
        dto.setNome(evento.getNome());
        dto.setDataRealizacao(evento.getDataRealizacao());
        dto.setCreditoInicialCliente(evento.getCreditoInicialCliente());
        return dto;
    }
}