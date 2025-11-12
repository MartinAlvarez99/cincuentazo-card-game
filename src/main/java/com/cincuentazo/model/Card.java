package com.cincuentazo.model;

public class Card {

    private String valor;
    private String simbolo;

    public Card(String valor, String simbolo) {
        this.valor = valor;
        this.simbolo = simbolo;
    }

    public String getValor() {
        return valor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    // Calcula el valor de la carta
    public int valorJuego(int sumaMesa, int valorAs) {

        if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
            return -10;
        }

        if (valor.equals("A")) {
            return valorAs;
        }

        if (valor.equals("9")) {
            return 0;
        }

        // para cartas (2-8, 10)
        return Integer.parseInt(valor);
    }

    @Override
    public String toString() {
        return valor + simbolo;
    }
}
