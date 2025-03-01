package com.divitibank.banco.Entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Date;

@Getter
@Setter
@Document(collection = "contacorrente")
public class ContaCorrente {
    @Id
    private String id;
    private String nome;
    private String sobrenome;
    private Date data_nascimento;
    private String cpf;
    private String email;
    private String senha;
    private double saldo;
    private List<Cartao> cartoes;
    private List<Extrato> extrato;

    public ContaCorrente (String nome, String sobrenome, String cpf, String email, String senha, double saldo, List<Cartao> cartoes, List<Extrato> extrato) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.saldo = saldo;
        this.cartoes = cartoes;
        this.extrato = extrato;
    }

    public ContaCorrente () {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public List<Cartao> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<Cartao> cartoes) {
        this.cartoes = cartoes;
    }

    public List<Extrato> getExtrato() {
        return extrato;
    }

    public void setExtrato(List<Extrato> extrato) {
        this.extrato = extrato;
    }

    public Date getdata_nascimento() {
        return this.data_nascimento;
    }

    public void setdata_nascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    @Override
    public String toString() {
        return "ContaCorrente [id=" + this.id + ", nome=" + this.nome + ", sobrenome=" + this.sobrenome + ", data_nascimento="
                + data_nascimento + ", cpf=" + cpf + ", email=" + email + ", senha=" + senha + ", saldo=" + this.saldo
                + ", cartoes=" + cartoes + ", extrato=" + extrato + "]";
    }
    
}
