package com.indracompany.treinamento.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "contas_bancarias")
public class ContaBancaria extends GenericEntity<Long> {

    private static final long serialVersionUID = 1315572533022942142L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 4, nullable = false)
    private String agencia;

    @Column(length = 6, nullable = false)
    private String numero;

    @Column(nullable = false)
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "fk_cliente_id")
    private Cliente cliente;
}