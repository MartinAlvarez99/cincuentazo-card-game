package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

public class Mesa {

    private List<Carta> cartas;
    private int suma;

    public Mesa() {
        cartas = new ArrayList<>();
        suma = 0;
    }

    public void ponerCarta(Carta c, int valorCarta) {
        cartas.add(c);
        suma += valorCarta;
    }

    public int getSuma() {
        return suma;
    }

    public Carta ultimaCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.get(cartas.size() - 1);
    }

}
