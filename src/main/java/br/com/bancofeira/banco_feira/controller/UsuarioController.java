package br.com.bancofeira.banco_feira.controller;

import br.com.bancofeira.banco_feira.dto.AlterarSenhaDto;
import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> criarUsuario(@RequestBody @Valid Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(novoUsuario);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Usuário criado com sucesso!", usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> buscarMeusDados(@AuthenticationPrincipal Usuario usuarioLogado) {
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(usuarioLogado);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Dados do usuário logado.", usuarioDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alterar-senha")
    public ResponseEntity<ApiResponse<Void>> alterarSenha(@AuthenticationPrincipal Usuario usuarioLogado, 
                                                          @RequestBody @Valid AlterarSenhaDto dadosSenha) {
        
        usuarioService.alterarSenha(usuarioLogado, dadosSenha.getSenhaAtual(), dadosSenha.getSenhaNova());

        ApiResponse<Void> response = new ApiResponse<>(true, "Senha alterada com sucesso!", null);
        return ResponseEntity.ok(response);
    }
}