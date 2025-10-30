package br.com.bancofeira.banco_feira.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.dto.InscricaoEmpresaDto;
import br.com.bancofeira.banco_feira.model.Empresa;
import br.com.bancofeira.banco_feira.model.Usuario;
import br.com.bancofeira.banco_feira.service.EmpresaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService){
        this.empresaService = empresaService;
    }


    @PostMapping("/inscrever")
    public ResponseEntity<Empresa> inscreverEmpresa(
            @RequestBody @Valid InscricaoEmpresaDto inscricaoDto,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        
        Empresa novaEmpresa = empresaService.inscreverEmpresa(inscricaoDto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }
    
}
