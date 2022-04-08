package com.indracompany.treinamento.model.service;

import java.util.ArrayList;
import java.util.List;

import com.indracompany.treinamento.model.dto.TransferenciaBancariaDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indracompany.treinamento.exception.AplicacaoException;
import com.indracompany.treinamento.exception.ExceptionValidacoes;
import com.indracompany.treinamento.model.dto.ConsultaContaBancariaDTO;
import com.indracompany.treinamento.model.entity.Cliente;
import com.indracompany.treinamento.model.entity.ContaBancaria;
import com.indracompany.treinamento.model.repository.ContaBancariaRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaBancariaService extends GenericCrudService<ContaBancaria, Long, ContaBancariaRepository>{

    @Autowired
    private ClienteService clienteService;

    public double consultarSaldo(String agencia, String numero) {
        return consultarConta(agencia, numero).getSaldo();
    }

    public ContaBancaria consultarConta (String agencia, String numeroConta) {

        ContaBancaria c = repository.findByAgenciaAndNumero(agencia, numeroConta);

        if (c == null) {
            throw new AplicacaoException(ExceptionValidacoes.ERRO_CONTA_INVALIDA);
        }

        return c;
    }

    public List<ConsultaContaBancariaDTO> obterContasPorCpf(String cpf){

        List<ConsultaContaBancariaDTO> listaContasRetorno = new ArrayList<>();
        Cliente cliente = clienteService.buscarCliente(cpf);

        List<ContaBancaria> listaContasCliente = repository.findByCliente(cliente);
        for (ContaBancaria conta : listaContasCliente) {
            ConsultaContaBancariaDTO dtoConta = new ConsultaContaBancariaDTO();
            BeanUtils.copyProperties(conta, dtoConta);
            dtoConta.setCpf(conta.getCliente().getCpf());
            dtoConta.setNomeTitular(conta.getCliente().getNome());
            listaContasRetorno.add(dtoConta);
        }

        return listaContasRetorno;
    }

    public void depositar(String agencia, String numeroConta, double valor) {
        ContaBancaria conta = consultarConta(agencia, numeroConta);
        conta.setSaldo(conta.getSaldo() + valor);
        super.salvar(conta);
    }

    public void sacar(String agencia, String numeroConta, double valor) {
        ContaBancaria conta = consultarConta(agencia, numeroConta);

        if(conta.getSaldo() < valor) {
            throw new AplicacaoException(ExceptionValidacoes.ERRO_SALDO_INEXISTENTE);
        }

        conta.setSaldo(conta.getSaldo() - valor);
        super.salvar(conta);
    }

    @Transactional(rollbackFor = Exception.class)
    public void transferir(TransferenciaBancariaDTO dto) {
        this.sacar(dto.getAgenciaOrigem(), dto.getNumeroContaOrigem(), dto.getValor());
        this.depositar(dto.getAgenciaOrigem(), dto.getNumeroContaDestino(), dto.getValor());
    }

}
