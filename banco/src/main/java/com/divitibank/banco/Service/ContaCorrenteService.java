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

    public ResponseEntity<Map<String, Object>> saveConta(ContaCorrente contaCorrente) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(contaCorrente.getCpf());
        Map<String, Object> response = new HashMap<>();
        if (contaTeste == null) {
            contaCorrenteRepository.save(contaCorrente);
            response.put("status", "sucesso");
            response.put("mensagem", "a conta foi inserida com sucesso");
            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "já existe uma conta com esse CPF");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ContaCorrente buscarPorCpf(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarPorCpf(cpf);
        return conta;
    }

    public ResponseEntity<Map<String, Object>> transferirDinheiro(String cpfDestino, String cpfRemetente, String metodo_pagamento, String dinheiro) {
        ContaCorrente contaDestino = contaCorrenteRepository.buscarPorCpf(cpfDestino);
        if (contaDestino == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "erro");
            response.put("mensagem", "cpf do destinatario não existe");
            return ResponseEntity.ok(response); 
        }else {
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
                    if (cartaoblue.getStatus().equals("ativo")) {
                        if ((cartaoblue.getCredito() >= dinheiroDouble)) {
                            contaDestino.setSaldo(contaDestino.getSaldo() + dinheiroDouble);
                        
                            Extrato extratoRemetente = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                            contaRemetente.getExtrato().add(extratoRemetente);
                            Extrato extratoDestino = new Extrato("recebido",contaRemetente.getNome() + " " + contaRemetente.getSobrenome(), dinheiroDouble, new Date());
                            contaDestino.getExtrato().add(extratoDestino);
                            
                            contaCorrenteRepository.atualizarExtrato(cpfRemetente, extratoRemetente);
                            contaCorrenteRepository.atualizarCreditoCartao(cpfRemetente, "blue", cartaoblue.getCredito() -  dinheiroDouble);
                            contaCorrenteRepository.save(contaDestino);
    
                            response.put("status", "sucesso");
                            response.put("mensagem", "Transferência realizada com sucesso");
    
                            return ResponseEntity.ok(response);                  
                        }else {
                            response.put("status", "erro");
                            response.put("mensagem", "Saldo insuficiente para a transferência");
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                        }
                    }else {
                        response.put("status", "erro");
                        response.put("mensagem", "o cartão está inativo");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                
                case "2":
                ContaCorrente cartoesBlack = contaCorrenteRepository.buscarCartaoPorCor(cpfRemetente,"black");
                Cartao cartaoblack = cartoesBlack.getCartoes().getFirst();
                if (cartaoblack.getStatus().equals("ativo")) {
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
                }else {
                    response.put("status", "erro");
                    response.put("mensagem", "o cartão esta inativo");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
                default:
                    response.put("status", "erro");
                    response.put("mensagem", "metodo de pagamento incorreto");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    
            }
        }
    
    }

    public Optional<List<Extrato>> buscarExtrato(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarExtratoPorUsuario(cpf);

        if (conta != null) {
            return Optional.ofNullable(conta.getExtrato());
        }
        return Optional.empty();
    }

    public ResponseEntity<Map<String, Object>> criarCartao(String cpf ,Cartao cartao) {
        ContaCorrente contaCorrente = contaCorrenteRepository.buscarPorCpf(cpf);
        Map<String, Object> response = new HashMap<>();
        if (contaCorrente != null) {
            contaCorrente.getCartoes().add(cartao);
            contaCorrenteRepository.save(contaCorrente);
            
            response.put("status", "sucesso");
            response.put("mensagem", "o cartão foi inserida com sucesso");
            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "não foi possivel encontrar essa conta");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
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

    public ResponseEntity<Map<String, Object>> buscarInformacoesPorCpf(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarInformacoesPorCPF(cpf);
        Map<String, Object> response = new HashMap<>();
        response.put("nome_sobrenome", conta.getNome() +" "+ conta.getSobrenome());
        response.put("data_nascimento", conta.getdata_nascimento());
        response.put("cpf_usuario", conta.getCpf());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> excluirContaPorId(String cpf) {
        ContaCorrente contaCorrente = contaCorrenteRepository.buscarPorCpf(cpf);
        Map<String, Object> response = new HashMap<>();
        if (contaCorrente != null) {
            contaCorrenteRepository.delete(contaCorrente);
            response.put("status", "sucesso");
            response.put("mensagem", "a conta foi deletada com sucesso");
            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "não existe uma conta com esse CPF");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> gerarComprovante(String cpfRemetente, String cpfDestino, double dinheiroTransferido) {
        ContaCorrente contaDestino = contaCorrenteRepository.buscarPorCpf(cpfDestino);
        ContaCorrente contaRemetente = contaCorrenteRepository.buscarPorCpf(cpfRemetente);
        Date dataTransferencia = new Date();

        Map<String, Object> response = new HashMap<>();
        response.put("nome_autor", contaRemetente.getNome() +" "+ contaRemetente.getSobrenome());
        response.put("cpf_autor", contaRemetente.getCpf());
        response.put("nome_destinatario", contaDestino.getNome() +" "+ contaDestino.getSobrenome());
        response.put("cpf_destinatario", contaDestino.getCpf());
        response.put("data_e_hora", dataTransferencia);
        response.put("dinheiro_transferido", Double.toString(dinheiroTransferido));

        return ResponseEntity.ok(response); 
    }

    public ResponseEntity<Map<String, Object>> atualizarStatusCartao(String cpf, String cor, String status) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        ContaCorrente cartaoPorCor = contaCorrenteRepository.buscarCartaoPorCor(cpf, cor);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            if (cartaoPorCor != null) {
                contaCorrenteRepository.atualizarStatusCartao(cpf, cor, status);

                response.put("status", "sucesso");
                response.put("mensagem", "o status foi atualizado com sucesso");
                response.put("status atual", status);

                return ResponseEntity.ok(response);
            } else {
                response.put("status", "erro");
                response.put("mensagem", "esse cartão não existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> atualizarCreditoCartao(String cpf, String cor, double novoCredito) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        ContaCorrente cartaoPorCor = contaCorrenteRepository.buscarCartaoPorCor(cpf, cor);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            if (cartaoPorCor != null) {
                Cartao cartao = cartaoPorCor.getCartoes().get(0);
                double dinheiro = cartao.getCredito() + novoCredito;

                contaCorrenteRepository.atualizarCreditoCartao(cpf, cor, dinheiro);

                response.put("status", "sucesso");
                response.put("mensagem", "o crédito foi atualizado com sucesso");
                response.put("credito atual", dinheiro);

                return ResponseEntity.ok(response);
            } else {
                response.put("status", "erro");
                response.put("mensagem", "esse cartão não existe");
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


}
