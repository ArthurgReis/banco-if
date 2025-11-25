package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.CreditosResponseDto;
import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.EventoPublicDto;
import br.com.bancofeira.banco_feira.dto.EventoRequestDto;
import br.com.bancofeira.banco_feira.dto.EventoResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Evento;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ApiResponse<EventoResponseDto>> criarEvento(@RequestBody @Valid EventoRequestDto eventoDto) {
        Evento novoEvento = eventoService.criarEvento(eventoDto);

        EventoResponseDto eventoResponseDto = EventoResponseDto.fromEntity(novoEvento);
        ApiResponse<EventoResponseDto> response = new ApiResponse<>(true, "Evento criado com sucesso!", eventoResponseDto);
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EventoPublicDto>> buscarEventoPorIdPublico(@PathVariable Integer id) {
        Evento eventoEntidade = eventoService.buscarEventoPorId(id);
        EventoPublicDto eventoDto = EventoPublicDto.fromEntity(eventoEntidade);
        ApiResponse<EventoPublicDto> response = new ApiResponse<>(true, "Evento encontrado.", eventoDto);
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/{eventoId}/empresas")
    public ResponseEntity<ApiResponse<List<EmpresaResponseDto>>> listarEmpresasDoEvento(@PathVariable Integer eventoId) {
        
        List<Empresa> empresas = eventoService.listarEmpresasPorEvento(eventoId);

        List<EmpresaResponseDto> dtos = empresas.stream()
                .map(EmpresaResponseDto::fromEntity)
                .collect(Collectors.toList());

        ApiResponse<List<EmpresaResponseDto>> response = new ApiResponse<>(true, "Empresas do evento listadas.", dtos);
        return ResponseEntity.ok(response);
    }

}