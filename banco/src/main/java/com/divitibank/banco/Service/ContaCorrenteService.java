package com.divitibank.banco.Service;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

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
                        response.put("saldo atual do remetente", contaRemetente.getSaldo());
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
                            contaCorrenteRepository.save(contaDestino);

                            if(cartaoblue.getTipo_cartao().equals("Crédito")) {
                                contaCorrenteRepository.atualizarFaturaCartao(cpfRemetente, "blue", cartaoblue.getFatura() + dinheiroDouble);

                                Extrato extratoCartao = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                                contaDestino.getExtrato().add(extratoDestino);
                                
                                contaCorrenteRepository.atualizarExtratoCartao(cpfRemetente, "blue", extratoCartao);
                                contaCorrenteRepository.atualizarCreditoCartao(cpfRemetente, "blue", cartaoblue.getCredito() -  dinheiroDouble);
                                response.put("saldo atual do cartão do remetente", cartaoblue.getCredito() - dinheiroDouble);
                            }else {
                                Random random = new Random();
                                int num = random.nextInt(1,100);
                                if (num <= 10) {
                                    dinheiroDouble = 0.95;
                                }
                                contaCorrenteRepository.atualizarSaldo(cpfRemetente, contaRemetente.getSaldo() - dinheiroDouble);
                                response.put("saldo atual do remetente", contaRemetente.getSaldo() - dinheiroDouble);
                            }
    
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
                        contaCorrenteRepository.save(contaDestino);

                        if(cartaoblack.getTipo_cartao().equals("Crédito")) {
                            contaCorrenteRepository.atualizarFaturaCartao(cpfRemetente, "black", cartaoblack.getFatura() + dinheiroDouble);

                            Extrato extratoCartao = new Extrato("gastos",contaDestino.getNome() + " " + contaDestino.getSobrenome(), -dinheiroDouble, new Date());
                            contaDestino.getExtrato().add(extratoDestino);

                            contaCorrenteRepository.atualizarExtratoCartao(cpfRemetente, "black", extratoCartao);
                            contaCorrenteRepository.atualizarCreditoCartao(cpfRemetente, "black", cartaoblack.getCredito() - dinheiroDouble);
                            response.put("saldo atual do cartão do remetente", cartaoblack.getCredito() - dinheiroDouble);
                        }else {
                            Random random = new Random();
                            int num = random.nextInt(1,100);
                            if (num <= 15) {
                                dinheiroDouble = 0.95;
                            }
                            contaCorrenteRepository.atualizarSaldo(cpfRemetente, contaRemetente.getSaldo() - dinheiroDouble);
                            response.put("saldo atual do remetente", contaRemetente.getSaldo() - dinheiroDouble);
                        }
                
    
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

    public ResponseEntity<Map<String, Object>> excluirCartaoPorCor(String cpf, String cor) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        ContaCorrente cartaoPorCor = contaCorrenteRepository.buscarCartaoPorCor(cpf, cor);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            if (cartaoPorCor != null) {
                contaCorrenteRepository.excluirCartao(cpf, cor);

                response.put("status", "sucesso");
                response.put("mensagem", "o cartao foi excluido com sucesso");

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

    public ResponseEntity<Map<String, Object>> mudarNome(String cpf, String nome) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            contaTeste.setNome(nome);
            contaCorrenteRepository.save(contaTeste);

            response.put("status", "sucesso");
            response.put("mensagem", "nome do usuario foi modificado com sucesso");
            response.put("nome atual", contaTeste.getNome());

            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> mudarSobrenome(String cpf, String sobrenome) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            contaTeste.setSobrenome(sobrenome);
            contaCorrenteRepository.save(contaTeste);

            response.put("status", "sucesso");
            response.put("mensagem", "o sobrenome do usuario foi modificado com sucesso");
            response.put("sobrenome atual", contaTeste.getSobrenome());

            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> mudarEmail(String cpf, String email) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            contaTeste.setEmail(email);
            contaCorrenteRepository.save(contaTeste);

            response.put("status", "sucesso");
            response.put("mensagem", "o e-mail do usuário foi modificado com sucesso");
            response.put("email atual", contaTeste.getEmail());

            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> mudarSenha(String cpf, String senha) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        
        Map<String, Object> response = new HashMap<>();
        if (contaTeste != null) {
            contaTeste.setSenha(senha);
            contaCorrenteRepository.save(contaTeste);

            response.put("status", "sucesso");
            response.put("mensagem", "a senha do usuário foi modificado com sucesso");
            response.put("email atual", contaTeste.getSenha());

            return ResponseEntity.ok(response);
        }else {
            response.put("status", "erro");
            response.put("mensagem", "essa conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> pagarFatura(String cpf, String cor) {
        ContaCorrente contaTeste = contaCorrenteRepository.buscarPorCpf(cpf);
        ContaCorrente cartoes = contaCorrenteRepository.buscarCartaoPorCor(cpf,cor);
        Cartao cartao = cartoes.getCartoes().getFirst();
        Map<String, Object> response = new HashMap<>();

        
        if (contaTeste != null) {
            if(cartao != null) {
                if (contaTeste.getSaldo() >= cartao.getFatura()) {

                    Extrato extratoDestino = new Extrato("gastos","fatura", (cartao.getFatura())*-1, new Date());
                    contaTeste.getExtrato().add(extratoDestino);
                    contaTeste.setSaldo(contaTeste.getSaldo() - cartao.getFatura());
                    cartao.setFatura(0);
                    
                    contaCorrenteRepository.save(contaTeste);
                    
                    response.put("status", "sucesso");
                    response.put("fatura atual", cartao.getFatura());
                    response.put("mensagem", "a fatura foi paga com sucesso");
                    
                    contaCorrenteRepository.atualizarFaturaCartao(cpf, cor, 0);
                    contaCorrenteRepository.limparExtratoCartao(cpf, cor);
        
                    return ResponseEntity.ok(response);
                }else {
                    response.put("status", "erro");
                    response.put("mensagem", "saldo insuficiente para pagar a fatura");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }else {
                response.put("status", "erro");
                response.put("mensagem", "esse cartão não existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }else {
            response.put("status", "erro");
            response.put("mensagem", "esse conta não existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public List<Extrato> exibirExtratoCartao(String cpf, String cor) {
        ContaCorrente conta = contaCorrenteRepository.buscarCartaoPorCor(cpf, cor);

        if (conta == null || conta.getCartoes() == null) {
            return Collections.emptyList();
        }

        return conta.getCartoes().stream().findFirst().map(Cartao::getExtrato).orElse(Collections.emptyList());
    }

}