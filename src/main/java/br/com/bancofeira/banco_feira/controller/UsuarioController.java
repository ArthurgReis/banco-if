package br.com.bancofeira.banco_feira.controller;


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

    /**
     * Endpoint PÚBLICO para criar um novo usuário (cliente).
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> criarUsuario(@RequestBody @Valid Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(novoUsuario);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Usuário criado com sucesso!", usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint PROTEGIDO para o usuário logado buscar seus próprios dados.
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> buscarMeusDados(@AuthenticationPrincipal Usuario usuarioLogado) {
        // Retorna o DTO do usuário que já foi carregado pelo Spring Security
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(usuarioLogado);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Dados do usuário logado.", usuarioDto);
        return ResponseEntity.ok(response);
    }

    // OS MÉTODOS listarTodos() E buscarPorId(id) FORAM REMOVIDOS.
    // A listagem de usuários agora é uma responsabilidade exclusiva do Admin.
}