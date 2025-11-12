package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.EventoPublicDto;
import br.com.bancofeira.banco_feira.dto.EventoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/eventos") 
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventoResponseDto>> criarEvento(@RequestBody @Valid Evento evento) {
        Evento novoEvento = eventoService.criarEvento(evento);
        EventoResponseDto eventoDto = EventoResponseDto.fromEntity(novoEvento);
        ApiResponse<EventoResponseDto> response = new ApiResponse<>(true, "Evento criado com sucesso!", eventoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventoPublicDto>>> listarEventos() {
        List<Evento> eventosEntidades = eventoService.listarEventos();

        List<EventoPublicDto> eventosDto = eventosEntidades.stream()
                .map(EventoPublicDto::fromEntity)
                .collect(Collectors.toList());

        ApiResponse<List<EventoPublicDto>> response = new ApiResponse<>(true, "Eventos listados com sucesso.", eventosDto);
        return ResponseEntity.ok(response);
    }

}