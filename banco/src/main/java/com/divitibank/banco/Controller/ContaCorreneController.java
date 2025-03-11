package com.divitibank.banco.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Service.ContaCorrenteService;

@RestController
@RequestMapping("/contas")
public class ContaCorreneController {
    private final ContaCorrenteService contaCorrenteService;

    public ContaCorreneController(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping("/criar")
    public ContaCorrente salvarConta(@RequestBody ContaCorrente contaCorrente) {
        return contaCorrenteService.saveConta(contaCorrente);
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
    public ContaCorrente buscarPorCpf(@PathVariable String cpf) {
        return contaCorrenteService.buscarInformacoesPorCpf(cpf);
    }

    @DeleteMapping("/{cpf}/excluir")
    public void deletarContaPorId(@PathVariable String cpf) {
        contaCorrenteService.excluirContaPorId(cpf);
    }

    @PostMapping("/transferir/{cpfRemetente}/{cpfDestino}/{dinheiro}")
    public boolean transferirDinheiro(@PathVariable String cpfRemetente, @PathVariable String cpfDestino, @PathVariable String dinheiro) {
        return contaCorrenteService.transferirDinheiro(cpfDestino, cpfRemetente, dinheiro);
    }
    

    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return new ResponseEntity<>("Ocorreu um erro!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
