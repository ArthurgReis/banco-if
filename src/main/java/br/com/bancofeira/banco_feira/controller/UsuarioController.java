package br.com.bancofeira.banco_feira.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario){
        Usuario novoUsuario = usuarioService.criarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);


    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/{id}/solicitar-acesso-empresa")
    public ResponseEntity<SolicitacaoAcessoEmpresa> solicitarAcessoEmpresa(@PathVariable Integer id) {
        SolicitacaoAcessoEmpresa solicitacao = usuarioService.solicitarAcessoEmpresa(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }


}
