package br.com.bancofeira.banco_feira.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "empresas")

public class Empresa {
    
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nome_fantasia", nullable=false, length=100)
    private String nomeFantasia;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private StatusEmpresa status = StatusEmpresa.PENDENTE;

    @Column(name="data_cadastro",nullable=false, updatable=false)
    private LocalDateTime dataCadastro;

    @ManyToMany(mappedBy = "empresas") 
    @JsonIgnoreProperties("empresas")
    private List<Usuario> funcionarios;

    @PrePersist
    protected void onCreate(){
        this.dataCadastro = LocalDateTime.now();
    }


}
