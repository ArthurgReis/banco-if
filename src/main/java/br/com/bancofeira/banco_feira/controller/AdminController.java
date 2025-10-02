package br.com.bancofeira.banco_feira.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PatchMapping("/solicitacoes/{id}/aprovar") 
    public ResponseEntity<SolicitacaoAcessoEmpresa> aprovarSolicitacao(@PathVariable Integer id) {
        SolicitacaoAcessoEmpresa solicitacaoAprovada = adminService.aprovarSolicitacao(id);
        return ResponseEntity.ok(solicitacaoAprovada);
    }

    @PatchMapping("/solicitacoes/{id}/rejeitar")
    public ResponseEntity<SolicitacaoAcessoEmpresa> rejeitarSolicitacao(@PathVariable Integer id) {
        SolicitacaoAcessoEmpresa solicitacaoRejeitada = adminService.rejeitarSolicitacao(id);
        return ResponseEntity.ok(solicitacaoRejeitada);
    }


}
