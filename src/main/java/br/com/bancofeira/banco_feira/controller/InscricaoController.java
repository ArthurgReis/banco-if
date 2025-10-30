package br.com.bancofeira.banco_feira.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.EmpresaResponseDto;
import br.com.bancofeira.banco_feira.dto.InscricaoClienteDto;
import br.com.bancofeira.banco_feira.dto.InscricaoClienteResponseDto;
import br.com.bancofeira.banco_feira.dto.InscricaoEmpresaDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.InscricaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/inscricoes") 
public class InscricaoController {

    private final InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @PostMapping("/cliente")
    public ResponseEntity<ApiResponse<InscricaoClienteResponseDto>> inscreverCliente(
            @RequestBody @Valid InscricaoClienteDto inscricaoDto, 
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Inscricao novaInscricao = inscricaoService.inscreverClienteEmEvento(
                inscricaoDto.getChaveInscricao(),
                usuarioLogado
        );
        InscricaoClienteResponseDto inscricaoResponseDto = InscricaoClienteResponseDto.fromEntity(novaInscricao);
        ApiResponse<InscricaoClienteResponseDto> response = new ApiResponse<>(true, "Inscrição no evento realizada com sucesso!", inscricaoResponseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/empresa")
    public ResponseEntity<ApiResponse<EmpresaResponseDto>> inscreverEmpresa(
            @RequestBody @Valid InscricaoEmpresaDto inscricaoDto, 
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Empresa novaEmpresa = inscricaoService.inscreverEmpresaEmEvento(inscricaoDto, usuarioLogado);
        EmpresaResponseDto empresaDto = EmpresaResponseDto.fromEntity(novaEmpresa);
        ApiResponse<EmpresaResponseDto> response = new ApiResponse<>(true, "Empresa inscrita no evento com sucesso!", empresaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}