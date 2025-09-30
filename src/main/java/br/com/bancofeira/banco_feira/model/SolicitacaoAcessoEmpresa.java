package br.com.bancofeira.banco_feira.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitacoes_acesso_empresa")
public class SolicitacaoAcessoEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    @Column(name = "data_solicitacao", nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @PrePersist
    protected void onCreate() {
        this.dataSolicitacao = LocalDateTime.now();
    }
}