package com.divitibank.banco.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Service.ContaCorrenteService;




@RestController
@RequestMapping("/contas")
public class ContaCorrenteController {
    private final ContaCorrenteService contaCorrenteService;

    public ContaCorrenteController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Map<String, Object>> salvarConta(@RequestBody ContaCorrente contaCorrente) {
        return contaCorrenteService.saveConta(contaCorrente);
    }

    @GetMapping("/buscarCartao/{cpf}/{cor}")
    public Optional<Cartao> buscarCartaoPorCor(@PathVariable String cpf, @PathVariable String cor) {
        return contaCorrenteService.buscarCartaoPorCor(cpf, cor);
    }
    
    @GetMapping("/{cpf}/cartoes")
    public Optional<List<Cartao>> buscarCartaoUsuario(@PathVariable String cpf) {
        return contaCorrenteService.buscarCartao(cpf);
    }

    @GetMapping("/{cpf}/extrato")
    public Optional<List<Extrato>> buscarExtratoUsuario(@PathVariable String cpf) {
        return contaCorrenteService.buscarExtrato(cpf);
    }

    @GetMapping("/{cpf}/informacoes")
    public ResponseEntity<Map<String, Object>> buscarPorCpf(@PathVariable String cpf) {
        return contaCorrenteService.buscarInformacoesPorCpf(cpf);
    }

    @DeleteMapping("/{cpf}/excluir")
    public ResponseEntity<Map<String, Object>> deletarContaPorId(@PathVariable String cpf) {
        return contaCorrenteService.excluirContaPorId(cpf);
    }

    @PutMapping("/transferir/{cpfRemetente}/{cpfDestino}/{dinheiro}/{metodo_pagamento}")
    public ResponseEntity<Map<String, Object>> transferirDinheiro(@PathVariable String cpfRemetente, @PathVariable String cpfDestino,@PathVariable String metodo_pagamento, @PathVariable String dinheiro) {
        return contaCorrenteService.transferirDinheiro(cpfDestino, cpfRemetente, metodo_pagamento, dinheiro);
    }

    @GetMapping("/buscar/{cpf}")
    public ContaCorrente buscarPrCpf(@PathVariable String cpf) {
        return contaCorrenteService.buscarPorCpf(cpf);
    }    

    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return new ResponseEntity<>("Ocorreu um erro!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/criar/cartao/{cpf}")
    public ResponseEntity<Map<String, Object>> registrarCartao(@PathVariable String cpf, @RequestBody Cartao cartao) {
        return contaCorrenteService.criarCartao(cpf, cartao);
    }

    @GetMapping("/gerarcomprovante/{cpfRemetente}/{cpfDestino}/{dinheiroTransferido}")
    public ResponseEntity<Map<String, Object>>  gerarComprovante(@PathVariable String cpfRemetente,@PathVariable String cpfDestino,@PathVariable double dinheiroTransferido) {
        return contaCorrenteService.gerarComprovante(cpfRemetente, cpfDestino, dinheiroTransferido);
    }
    
    @PutMapping("/mudarstatus/{cpf}/{cor}/{status}")
    public ResponseEntity<Map<String, Object>> atualizarStatusCartao(@PathVariable String cpf,@PathVariable String cor,@PathVariable String status) {
       return contaCorrenteService.atualizarStatusCartao(cpf, cor, status);
    }

    @PutMapping("/mudarcredito/{cpf}/{cor}/{novoCredito}")
    public ResponseEntity<Map<String, Object>> atualizarCreditoCartao(@PathVariable String cpf,@PathVariable String cor,@PathVariable double novoCredito) {
        return contaCorrenteService.atualizarCreditoCartao(cpf, cor, novoCredito);
    }

    @PutMapping("/mudarnome/{cpf}/{nome}")
    public ResponseEntity<Map<String, Object>> mudarNome(@PathVariable String cpf, @PathVariable String nome) {
        return contaCorrenteService.mudarNome(cpf, nome);
    }

    @PutMapping("/mudarsobrenome/{cpf}/{sobrenome}")
    public ResponseEntity<Map<String, Object>> mudarSobrenome(@PathVariable String cpf, @PathVariable String sobrenome) {
        return contaCorrenteService.mudarSobrenome(cpf, sobrenome);
    }

    @PutMapping("/mudaremail/{cpf}/{email}")
    public ResponseEntity<Map<String, Object>> mudarEmail(@PathVariable String cpf, @PathVariable String email) {
        return contaCorrenteService.mudarSobrenome(cpf, email);
    }

    @PutMapping("/mudarsenha/{cpf}/{senha}")
    public ResponseEntity<Map<String, Object>> mudarSenha(@PathVariable String cpf, @PathVariable String senha) {
        return contaCorrenteService.mudarSenha(cpf, senha);
    }

    @DeleteMapping("/excluircartao/{cpf}/{cor}")
    public ResponseEntity<Map<String, Object>> excluirCartaoPorCor(@PathVariable String cpf, @PathVariable String cor) {
        return contaCorrenteService.excluirCartaoPorCor(cpf, cor);
    }

    @PutMapping("/pagarfatura/{cpf}/{cor}")
    public ResponseEntity<Map<String, Object>> pagarFatura(@PathVariable String cpf, @PathVariable String cor) {
        return contaCorrenteService.pagarFatura(cpf, cor);
    }

    @GetMapping("mostrarextratocartao/{cpf}/{cor}")
    public List<Extrato> mostarExtratoCartao(@PathVariable String cpf, @PathVariable String cor) {
        return contaCorrenteService.exibirExtratoCartao(cpf, cor);
    }
    
}
