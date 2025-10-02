package br.com.bancofeira.banco_feira.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService){
        this.empresaService = empresaService;
    }

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Empresa> criarEmpresa(@PathVariable Integer usuarioId, @RequestBody Empresa empresa){
        Empresa novaEmpresa = empresaService.criarEmpresa(empresa, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }
    
}
