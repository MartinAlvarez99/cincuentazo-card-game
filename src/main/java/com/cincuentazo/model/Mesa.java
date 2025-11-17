package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la mesa central donde se colocan las cartas jugadas.
 *
 * <p>Mantiene una lista con las cartas que se han ido jugando y la suma acumulada
 * según las reglas del juego (la cual determina si una carta puede o no ser jugada).</p>
 */
public class Mesa {

    /** Cartas que han sido colocadas en la mesa en orden de juego. */
    private List<Carta> cartas;

    /** Suma acumulada resultante de los valores de juego de las cartas colocadas. */
    private int suma;

    /**
     * Crea una mesa vacía con suma inicial de 0.
     */
    public Mesa() {
        cartas = new ArrayList<>();
        suma = 0;
    }

    /**
     * Coloca una carta en la mesa y actualiza la suma acumulada con el valor
     * correspondiente al turno.
     *
     * @param c la carta jugada.
     * @param valorCarta el valor que esa carta aporta según {@link Carta#valorJuego}.
     */
    public void ponerCarta(Carta c, int valorCarta) {
        cartas.add(c);
        suma += valorCarta;
    }

    /**
     * Obtiene la suma actual de la mesa.
     *
     * @return suma de valores acumulados.
     */
    public int getSuma() {
        return suma;
    }

    /**
     * Devuelve la última carta colocada en la mesa.
     *
     * @return la carta más reciente, o {@code null} si aún no hay cartas.
     */
    public Carta ultimaCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.get(cartas.size() - 1);
    }
}
