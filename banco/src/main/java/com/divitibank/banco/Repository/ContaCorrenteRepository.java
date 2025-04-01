package com.divitibank.banco.Repository;

import com.divitibank.banco.Entity.ContaCorrente;
import com.divitibank.banco.Entity.Extrato;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaCorrenteRepository extends MongoRepository<ContaCorrente, String> {

    @Query(value = "{'cpf': ?0}", fields = "{'_id': 0,'cartoes': 1}")
    ContaCorrente buscarCartaoPorUsuario(String cpf);

    @Query(value = "{'cpf': ?0, 'cartoes.cor_cartao': ?1}", fields = "{'_id': 1,'cartoes': {'$elemMatch': {'cor_cartao': ?1}}}")
    ContaCorrente buscarCartaoPorCor(String cpf, String cor);

    @Query(value= "{'cpf': ?0}", fields = "{'_id': 0,'extrato': 1}")
    ContaCorrente buscarExtratoPorUsuario(String cpf);

    @Query(value = "{'cpf':  ?0}", fields = "{'_id':  0, 'nome': 1,'sobrenome': 1,'cpf': 1,'data_nascimento': 1}")
    ContaCorrente buscarInformacoesPorCPF(String cpf);

    @Query(value = "{'cpf': ?0}")
    ContaCorrente buscarPorCpf(String cpf);

    @Query("{ 'cpf': ?0, 'cartoes.cor_cartao': ?1 }")
    @Update("{ '$set': { 'cartoes.$.credito': ?2 } }")
    void atualizarCreditoCartao(String cpf, String cor, double novoCredito);

    @Query("{ 'cpf': ?0, 'cartoes.cor_cartao': ?1 }")
    @Update("{ '$set': { 'cartoes.$.fatura': ?2 } }")
    void atualizarFaturaCartao(String cpf, String cor, double novaFatura);

    @Query("{ 'cpf': ?0, 'cartoes.cor_cartao': ?1 }")
    @Update("{ '$set': { 'cartoes.$.status': ?2 } }")
    void atualizarStatusCartao(String cpf, String cor, String status);

    @Query("{ 'cpf': ?0}")
    @Update("{ '$push': { 'extrato': ?1 } }")
    void atualizarExtrato(String cpf, Extrato extrato);

    @Query("{ 'cpf': ?0, 'cartoes.cor_cartao': ?1}")
    @Update("{ '$push': { 'cartoes.$.extrato': ?2 } }")
    void atualizarExtratoCartao(String cpf, String cor, Extrato extrato);

    @Query("{ 'cpf': ?0 }")
    @Update("{ '$pull': { 'cartoes': { 'cor_cartao': ?1 } } }")
    void excluirCartao(String cpf, String cor);

    @Query("{ 'cpf': ?0, 'cartoes.cor_cartao': ?1}")
    @Update("{ $set: { 'cartoes.$.extrato': [] } }")
    void limparExtratoCartao(String cpf, String cor);
}
