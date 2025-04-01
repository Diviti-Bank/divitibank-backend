package com.divitibank.banco.Entity;

import java.util.List;

public class Cartao {
    private String status;
    private double credito;
    private String tipo_cartao;
    private String cor_cartao;
    private boolean aproximacao;
    private Double fatura;
    private List<Extrato> extrato;
    private int cvc;
    private String nome_cartao;
    private String numero_cartao;
    private String validade;
    

    public Cartao(String status, double credito, String tipo_cartao, String cor_cartao, boolean aproximacao,
            Double fatura, int cvc, String nome_cartao, String numero_cartao, String validade, List<Extrato> extrato) {
        this.status = status;
        this.credito = credito;
        this.tipo_cartao = tipo_cartao;
        this.cor_cartao = cor_cartao;
        this.aproximacao = aproximacao;
        this.extrato = extrato;
        this.fatura = fatura;
        this.cvc = cvc;
        this.nome_cartao = nome_cartao;
        this.numero_cartao = numero_cartao;
        this.validade = validade;
    }



    public Cartao() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getCredito() {
        return credito;
    }

    public void setCredito(double credito) {
        this.credito = credito;
    }

    public String getTipo_cartao() {
        return tipo_cartao;
    }

    public void setTipo_cartao(String tipo_cartao) {
        this.tipo_cartao = tipo_cartao;
    }

    public boolean isAproximacao() {
        return aproximacao;
    }

    public void setAproximacao(boolean aproximacao) {
        this.aproximacao = aproximacao;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public String getNome_cartao() {
        return nome_cartao;
    }

    public void setNome_cartao(String nome_cartao) {
        this.nome_cartao = nome_cartao;
    }

    public String getNumero_cartao() {
        return numero_cartao;
    }

    public void setNumero_cartao(String numero_cartao) {
        this.numero_cartao = numero_cartao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getCor_cartao() {
        return cor_cartao;
    }

    public void setCor_cartao(String cor_cartao) {
        this.cor_cartao = cor_cartao;
    }

    public double getFatura() {
        return fatura;
    }

    public void setFatura(double fatura) {
        this.fatura = fatura;
    }  

    public List<Extrato> getExtrato() {
        return extrato;
    }

    public void setExtrato(List<Extrato> extrato) {
        this.extrato = extrato;
    }

    @Override
    public String toString() {
        return "Cartao [status=" + status + ", credito=" + credito + ", tipo_cartao=" + tipo_cartao + ", cor_cartao="
                + cor_cartao + ", aproximacao=" + aproximacao + ", cvc=" + cvc + ", nome_cartao=" + nome_cartao
                + ", numero_cartao=" + numero_cartao + ", validade=" + validade + ", fatura=" + fatura + ", extrato=" + extrato + "]";
    }
}
