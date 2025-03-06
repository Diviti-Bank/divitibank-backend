package com.divitibank.banco.Controller;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Service.ContaCorrenteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
public class ContaCorreneController {
    private ContaCorrenteService contaCorrenteService;

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

    @DeleteMapping("/{cpf}")
    public void deletarContaPorId(@PathVariable String cpf) {
        contaCorrenteService.excluirContaPorId(cpf);
    }

    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
