package com.divitibank.banco.Repository;

import com.divitibank.banco.Entity.ContaCorrente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaCorrenteRepository extends MongoRepository<ContaCorrente, String> {
    @Query(value = "{'cpf': ?0}", fields = "{'_id': 0,'cartoes': 1}")
    ContaCorrente buscarCartaoPorUsuario(String cpf);
    @Query(value= "{'cpf': ?0}", fields = "{'_id': 0,'extrato': 1}")
    ContaCorrente buscarExtratoPorUsuario(String cpf);
    @Query(value = "{'cpf':  ?0}", fields = "{'_id':  0, 'nome': 1,'sobrenome': 1,'cpf': 1,'data_nascimento': 1}")
    ContaCorrente buscarInformacoesPorCPF(String cpf);
}
