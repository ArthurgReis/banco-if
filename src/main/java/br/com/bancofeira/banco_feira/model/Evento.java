package br.com.bancofeira.banco_feira.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_realizacao")
    private LocalDate dataRealizacao;

    @Column(name = "chave_inscricao", nullable = false, unique = true, length = 8)
    private String chaveInscricao;

    @Column(name = "credito_inicial_cliente", nullable = false)
    private BigDecimal creditoInicialCliente = BigDecimal.ZERO;

    @OneToMany(mappedBy = "evento")
    private List<Empresa> empresas;
}