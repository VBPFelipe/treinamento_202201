package com.indracompany.treinamento.controller.rest;

import com.indracompany.treinamento.model.dto.ConsultaContaBancariaDTO;
import com.indracompany.treinamento.model.dto.DepositoDTO;
import com.indracompany.treinamento.model.dto.SaqueDTO;
import com.indracompany.treinamento.model.dto.TransferenciaBancariaDTO;
import com.indracompany.treinamento.model.entity.ContaBancaria;
import com.indracompany.treinamento.model.service.ContaBancariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/contas")
public class ContaBancariaRest extends GenericCrudRest<ContaBancaria, Long, ContaBancariaService>{

    @Autowired
    private ContaBancariaService contaBancariaService;

    @GetMapping(value = "/consultar-saldo/{agencia}/{numeroConta}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Double> consultarSaldo(String agencia, String numeroConta){
        double saldo = contaBancariaService.consultarSaldo(agencia, numeroConta);
        return new ResponseEntity<>(saldo, HttpStatus.OK);
    }

    @GetMapping(value = "/consultar-contas-por-cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<List<ConsultaContaBancariaDTO>> consultarContasPorCpf(@PathVariable String cpf){
        List<ConsultaContaBancariaDTO> contas = contaBancariaService.obterContasPorCpf(cpf);
        if (contas == null || contas.isEmpty()) {
            return new ResponseEntity<List<ConsultaContaBancariaDTO>>(contas, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<ConsultaContaBancariaDTO>>(contas, HttpStatus.OK);
    }

    @PutMapping(value = "/deposito", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Void> depositar(@RequestBody DepositoDTO dto){
        contaBancariaService.depositar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/saque", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Void> sacar(@RequestBody SaqueDTO dto){
        contaBancariaService.sacar(dto.getAgencia(), dto.getNumeroConta(), dto.getValor());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PutMapping(value = "/transferencia", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Void> sacar(@RequestBody TransferenciaBancariaDTO dto){
        contaBancariaService.transferir(dto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
