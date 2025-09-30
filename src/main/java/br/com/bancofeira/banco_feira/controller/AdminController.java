package br.com.bancofeira.banco_feira.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping("/solicitacoes/pendentes")
    public ResponseEntity<List<SolicitacaoAcessoEmpresa>> listarSolicitacoesPendentes(){
        List<SolicitacaoAcessoEmpresa> solicitacoes = adminService.listarSolicitacoesPendentes();
        return ResponseEntity.ok(solicitacoes);
    }


}
