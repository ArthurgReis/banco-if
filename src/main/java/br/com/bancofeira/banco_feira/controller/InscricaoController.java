package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.InscricaoClienteDto;
import br.com.bancofeira.banco_feira.dto.InscricaoClienteResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Inscricao;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.InscricaoService;
import br.com.bancofeira.banco_feira.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private final InscricaoService inscricaoService;
    private final JwtService jwtService;

    public InscricaoController(InscricaoService inscricaoService, JwtService jwtService) {
        this.inscricaoService = inscricaoService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cliente")
    public ResponseEntity<ApiResponse<InscricaoClienteResponseDto>> inscreverCliente(
            @RequestBody @Valid InscricaoClienteDto inscricaoDto,
            @AuthenticationPrincipal Usuario usuarioLogado) {

        Inscricao novaInscricao = inscricaoService.inscreverClienteEmEvento(
                inscricaoDto.getChaveInscricao(),
                usuarioLogado
        );

        String novoToken = jwtService.generateToken(usuarioLogado);

        InscricaoClienteResponseDto inscricaoResponseDto = InscricaoClienteResponseDto.fromEntity(novaInscricao);

        ApiResponse<InscricaoClienteResponseDto> response = new ApiResponse<>(
                true,
                "Inscrição realizada com sucesso!",
                inscricaoResponseDto,
                novoToken
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}