package com.indracompany.treinamento.model.repository;

import com.indracompany.treinamento.model.entity.Cliente;
import com.indracompany.treinamento.model.entity.ContaBancaria;

import java.util.List;

public interface ContaBancariaRepository extends GenericCrudRepository<ContaBancaria, Long>{

    List<ContaBancaria> findByCliente(Cliente cli);

}