package com.divitibank.banco.Service;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ContaCorrenteService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    public ContaCorrente saveConta(ContaCorrente contaCorrente) {
        return contaCorrenteRepository.save(contaCorrente);
    }

    public ContaCorrente buscarPorCpf(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarPorCpf(cpf);
        return conta;
    }

    public ResponseEntity<Map<String, Object>> transferirDinheiro(String cpfDestino, String cpfRemetente, String metodo_pagamento, String dinheiro) {
        ContaCorrente contaDestino = contaCorrenteRepository.buscarPorCpf(cpfDestino);
        ContaCorrente contaRemetente = contaCorrenteRepository.buscarPorCpf(cpfRemetente);
        double dinheiroDouble = Double.parseDouble(dinheiro);
        
        Map<String, Object> response = new HashMap<>();

        
        switch (metodo_pagamento) {
            case "0":
                if (contaRemetente.getSaldo() >= dinheiroDouble) {
                    contaRemetente.setSaldo(contaRemetente.getSaldo() - dinheiroDouble);
                    contaDestino.setSaldo(contaDestino.getSaldo() + dinheiroDouble);
                    
                    Extrato extratoRemetente = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                    contaRemetente.getExtrato().add(extratoRemetente);
                    Extrato extratoDestino = new Extrato("recebido",contaRemetente.getNome() + " " + contaRemetente.getSobrenome(), dinheiroDouble, new Date());
                    contaDestino.getExtrato().add(extratoDestino);
                    
                    contaCorrenteRepository.save(contaRemetente);
                    contaCorrenteRepository.save(contaDestino);

                    response.put("status", "sucesso");
                    response.put("mensagem", "Transferência realizada com sucesso");

                    return ResponseEntity.ok(response);
                }else {
                    response.put("status", "erro");
                    response.put("mensagem", "Saldo insuficiente para a transferência");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            case "1":
                ContaCorrente cartoes = contaCorrenteRepository.buscarCartaoPorCor(cpfRemetente,"blue");
                Cartao cartaoblue = cartoes.getCartoes().getFirst();
                if (cartaoblue.getCredito() >= dinheiroDouble) {
                    contaDestino.setSaldo(contaDestino.getSaldo() + dinheiroDouble);
                    
                    Extrato extratoRemetente = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                    contaRemetente.getExtrato().add(extratoRemetente);
                    Extrato extratoDestino = new Extrato("recebido",contaRemetente.getNome() + " " + contaRemetente.getSobrenome(), dinheiroDouble, new Date());
                    contaDestino.getExtrato().add(extratoDestino);
                    
                    contaCorrenteRepository.atualizarExtrato(cpfRemetente, extratoRemetente);
                    contaCorrenteRepository.atualizarCreditoCartao(cpfRemetente, "blue", cartaoblue.getCredito() - dinheiroDouble);
                    contaCorrenteRepository.save(contaDestino);

                    response.put("status", "sucesso");
                    response.put("mensagem", "Transferência realizada com sucesso");

                    return ResponseEntity.ok(response);                  
                }else {
                    response.put("status", "erro");
                    response.put("mensagem", "Saldo insuficiente para a transferência");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            case "2":
            ContaCorrente cartoesBlack = contaCorrenteRepository.buscarCartaoPorCor(cpfRemetente,"black");
            Cartao cartaoblack = cartoesBlack.getCartoes().getFirst();
            if (cartaoblack.getCredito() >= dinheiroDouble) {
                contaDestino.setSaldo(contaDestino.getSaldo() + dinheiroDouble);
                
                Extrato extratoRemetente = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                contaRemetente.getExtrato().add(extratoRemetente);
                Extrato extratoDestino = new Extrato("recebido",contaRemetente.getNome() + " " + contaRemetente.getSobrenome(), dinheiroDouble, new Date());
                contaDestino.getExtrato().add(extratoDestino);
                
                contaCorrenteRepository.atualizarExtrato(cpfRemetente, extratoRemetente);
                contaCorrenteRepository.atualizarCreditoCartao(cpfRemetente, "blue", cartaoblack.getCredito() - dinheiroDouble);
                contaCorrenteRepository.save(contaDestino);

                response.put("status", "sucesso");
                response.put("mensagem", "Transferência realizada com sucesso");

                return ResponseEntity.ok(response);                  
            }else {
                response.put("status", "erro");
                response.put("mensagem", "Saldo insuficiente para a transferência");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            default:
                response.put("status", "erro");
                response.put("mensagem", "metodo de pagamento incorreto");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    
        }
    }

    public Optional<List<Extrato>> buscarExtrato(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarExtratoPorUsuario(cpf);

        if (conta != null) {
            return Optional.ofNullable(conta.getExtrato());
        }
        return Optional.empty();
    }

    public ContaCorrente criarCartao(String cpf ,Cartao cartao) {
        ContaCorrente contaCorrente = contaCorrenteRepository.buscarPorCpf(cpf);
        contaCorrente.getCartoes().add(cartao);
        return contaCorrenteRepository.save(contaCorrente);
    }

    public Optional<List<Cartao>> buscarCartao(String cpf) {
        ContaCorrente conta  = contaCorrenteRepository.buscarCartaoPorUsuario(cpf);

        if(conta != null) {
            return Optional.ofNullable(conta.getCartoes());
        }
        return Optional.empty();
    }

    public Optional<Cartao> buscarCartaoPorCor(String cpf, String cor) {
        ContaCorrente conta = contaCorrenteRepository.buscarCartaoPorCor(cpf, cor);
    
        if (conta != null && conta.getCartoes() != null) {
            return conta.getCartoes().stream().findFirst();
        }
        return Optional.empty();
    }

    public ContaCorrente buscarInformacoesPorCpf(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarInformacoesPorCPF(cpf);
        return conta;
    }

    public void excluirContaPorId(String cpf) {
        ContaCorrente contaCorrente = contaCorrenteRepository.buscarPorCpf(cpf);
        contaCorrenteRepository.delete(contaCorrente);
    }


}
