package br.com.bancofeira.banco_feira.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.UsuarioResponseDto;
import br.com.bancofeira.banco_feira.model.ApiResponse;
import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    private UsuarioResponseDto toDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setCpf(usuario.getCpf());
        dto.setCreditos(usuario.getCreditos());
        dto.setRoles(usuario.getRoles());
        return dto;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> criarUsuario(@RequestBody @Valid Usuario usuario) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        UsuarioResponseDto usuarioDto = toDto(novoUsuario);
        ApiResponse<UsuarioResponseDto> resposta = new ApiResponse<>(true, "Usuário criado com sucesso!", usuarioDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
}

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarTodos(){
        List<Usuario> usuariosEntidades = usuarioService.listarTodos();

        List<UsuarioResponseDto> usuariosDto = usuariosEntidades.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(usuariosDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(toDto(usuario));
    }

    @PostMapping("/{id}/solicitar-acesso-empresa")
    public ResponseEntity<SolicitacaoAcessoEmpresa> solicitarAcessoEmpresa(@PathVariable Integer id) {
        SolicitacaoAcessoEmpresa solicitacao = usuarioService.solicitarAcessoEmpresa(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }


}
