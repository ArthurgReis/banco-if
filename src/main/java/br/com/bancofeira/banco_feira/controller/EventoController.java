package br.com.bancofeira.banco_feira.controller;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.EventoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Evento; 
import br.com.bancofeira.banco_feira.service.EventoService;
import jakarta.validation.Valid;

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

}