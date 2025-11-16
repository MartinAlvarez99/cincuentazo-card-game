package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

public class Jugador {

    private String nombre;
    private List<Carta> mano;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    public void agregarCarta(Carta c) {
        mano.add(c);
    }

    public void quitarCarta(Carta c) {
        mano.remove(c);
    }

    public List<Carta> getMano() {
        return mano;
    }

    public String getNombre() {
        return nombre;
    }

    // Devuelve las cartas que se pueden jugar sin pasar de 50

    public List<Carta> cartasJugables(int sumaMesa) {
        List<Carta> jugables = new ArrayList<>();
        for (Carta c : mano) {
            int valor = 0;
            if (c.getValor().equals("A")) {
                // el As puede valer 1 o 10, se revisan los dos casos
                if (sumaMesa + 1 <= 50 || sumaMesa + 10 <= 50) {
                    jugables.add(c);
                }
            } else {
                valor = c.valorJuego(sumaMesa, 0);
                if (sumaMesa + valor <= 50) {
                    jugables.add(c);
                }
            }
        }
        return jugables;
    }
}
