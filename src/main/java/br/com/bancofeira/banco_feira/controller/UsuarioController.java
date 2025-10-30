package br.com.bancofeira.banco_feira.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.UsuarioService;
import jakarta.validation.Valid;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioResponseDto>>> listarTodos() {
        List<Usuario> usuariosEntidades = usuarioService.listarTodos();
        List<UsuarioResponseDto> usuariosDto = usuariosEntidades.stream()
                .map(UsuarioResponseDto::fromEntity)
                .collect(Collectors.toList());
        ApiResponse<List<UsuarioResponseDto>> response = new ApiResponse<>(true, "Usuários listados com sucesso.", usuariosDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> buscarPorId(@PathVariable Integer id) {
        Usuario usuarioEntidade = usuarioService.buscarPorId(id);
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(usuarioEntidade);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Usuário encontrado.", usuarioDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> buscarMeusDados(@AuthenticationPrincipal Usuario usuarioLogado) {
        UsuarioResponseDto usuarioDto = UsuarioResponseDto.fromEntity(usuarioLogado);
        ApiResponse<UsuarioResponseDto> response = new ApiResponse<>(true, "Dados do usuário logado.", usuarioDto);
        return ResponseEntity.ok(response);
    }

}