package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {

    private List<Card> cartas;

    public Mazo() {
        cartas = new ArrayList<>();

        // valores y símbolos del póker
        String[] valores = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] simbolos = {"♠","♥","♦","♣"};

        // crear todas las cartas del mazo
        for (String s : simbolos) {
            for (String v : valores) {
                cartas.add(new Card(v, s));
            }
        }

        // mezclar las cartas para que salgan aleatorias
        Collections.shuffle(cartas);
    }

    // saca una carta del mazo (la primera)
    public Card sacarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    // saber si el mazo ya se acabó
    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    // vuelve a mezclar el mazo
    public void barajar() {
        Collections.shuffle(cartas);
    }

    // devuelve cuántas cartas quedan
    public int cartasRestantes() {
        return cartas.size();
    }

    // por si necesitas acceder a todas las cartas
    public List<Card> getCartas() {
        return cartas;
    }
}