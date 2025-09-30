package br.com.bancofeira.banco_feira.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.bancofeira.banco_feira.model.SolicitacaoAcessoEmpresa;
import br.com.bancofeira.banco_feira.model.StatusSolicitacao;

public interface SolicitacaoAcessoEmpresaRepository extends JpaRepository<SolicitacaoAcessoEmpresa, Integer> {
    Optional<SolicitacaoAcessoEmpresa> findByUsuarioIdAndStatus(Integer usuarioId, StatusSolicitacao status);
}