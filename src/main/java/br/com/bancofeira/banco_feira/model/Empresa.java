package br.com.bancofeira.banco_feira.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome fantasia não pode estar em branco.") 
    @Column(name = "nome_fantasia", nullable = false, length = 100)
    private String nomeFantasia;

    @Column(name = "descricao_curta")
    private String descricaoCurta;

    @Column(nullable = false)
    private BigDecimal creditos = BigDecimal.ZERO;


    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "chave_funcionario", nullable = false, unique = true, length = 8) 
    private String chaveFuncionario;

    @ManyToMany(mappedBy = "empresas")
    @JsonIgnoreProperties("empresas") 
    private List<Usuario> funcionarios;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    @JsonIgnoreProperties("empresas")
    private Evento evento;

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDateTime.now();
    }
}