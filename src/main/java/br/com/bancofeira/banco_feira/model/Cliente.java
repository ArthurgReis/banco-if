package br.com.bancofeira.banco_feira.model;

import java.math.BigDecimal;
import java.util.List;
import org.hibernate.validator.constraints.br.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O CPF co cliente não pode estar em branco")
    @CPF(message = "O formato de CPF está inválido")
    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "creditos_pre_compra", nullable = false) 
    private BigDecimal saldo = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos;
}