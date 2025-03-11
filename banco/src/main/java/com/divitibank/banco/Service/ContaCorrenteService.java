package com.divitibank.banco.Service;

import com.divitibank.banco.Entity.Cartao;
import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;
import com.divitibank.banco.Repository.ContaCorrenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContaCorrenteService {

    @Autowired
    private ContaCorrenteRepository contaCorrenteRepository;

    public ContaCorrente saveConta(ContaCorrente contaCorrente) {
        return contaCorrenteRepository.save(contaCorrente);
    }

    public boolean transferirDinheiro(String cpfDestino, String cpfRemetente, String dinheiro) {
        ContaCorrente contaDestino = contaCorrenteRepository.buscarPorCpf(cpfDestino);
        ContaCorrente contaRemetente = contaCorrenteRepository.buscarPorCpf(cpfRemetente);
        double dinheiroDouble = Double.parseDouble(dinheiro);

        if((contaDestino != null)&&(contaRemetente != null)) {
            if (contaRemetente.getSaldo() >= dinheiroDouble) {
                contaRemetente.setSaldo(contaRemetente.getSaldo() - dinheiroDouble);
                contaDestino.setSaldo(contaDestino.getSaldo() + dinheiroDouble);
                contaCorrenteRepository.save(contaRemetente);
                contaCorrenteRepository.save(contaDestino);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public Optional<List<Extrato>> buscarExtrato(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarExtratoPorUsuario(cpf);

        if (conta != null) {
            return Optional.ofNullable(conta.getExtrato());
        }
        return Optional.empty();
    }

    public Optional<List<Cartao>> buscarCartao(String cpf) {
        ContaCorrente conta  = contaCorrenteRepository.buscarCartaoPorUsuario(cpf);

        if(conta != null) {
            return Optional.ofNullable(conta.getCartoes());
        }
        return Optional.empty();
    }

    public ContaCorrente buscarInformacoesPorCpf(String cpf) {
        ContaCorrente conta = contaCorrenteRepository.buscarInformacoesPorCPF(cpf);
        return conta;
    }
    public void excluirContaPorId(String cpf) {
        contaCorrenteRepository.deleteById(cpf);
    }


}
