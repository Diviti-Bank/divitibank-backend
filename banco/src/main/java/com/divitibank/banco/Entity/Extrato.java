package com.divitibank.banco.Entity;

import java.util.Date;

public class Extrato {
    private String tipo;
    private String origem;
    private double quantia;
    private Date data;

    public Extrato(String tipo, String origem, double quantia, Date data) {
        this.tipo = tipo;
        this.origem = origem;
        this.quantia = quantia;
        this.data = data;
    }

    public Extrato() {

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public double getQuantia() {
        return quantia;
    }

    public void setQuantia(double quantia) {
        this.quantia = quantia;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Extrato{" +
                "tipo='" + tipo + '\'' +
                ", origem='" + origem + '\'' +
                ", quantia=" + quantia +
                ", data=" + data +
                '}';
    }
}
