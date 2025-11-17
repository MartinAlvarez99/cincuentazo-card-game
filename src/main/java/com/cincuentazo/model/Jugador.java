package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo del jugador humano.
 *
 * <p>Contiene el nombre y la mano de cartas, y provee utilidades para agregar,
 * quitar y consultar cartas en la mano. También permite determinar qué cartas
 * son jugables de acuerdo con la suma actual de la mesa.</p>
 */
public class Jugador {

    /** Nombre del jugador. */
    private String nombre;

    /** Colección de cartas que el jugador tiene actualmente en su mano. */
    private List<Carta> mano;

    /**
     * Crea un jugador con un nombre y una mano inicialmente vacía.
     *
     * @param nombre nombre asignado al jugador.
     */
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    /**
     * Agrega una carta a la mano del jugador.
     *
     * @param c carta a agregar.
     */
    public void agregarCarta(Carta c) {
        mano.add(c);
    }

    /**
     * Remueve una carta de la mano del jugador.
     *
     * @param c carta a quitar.
     */
    public void quitarCarta(Carta c) {
        mano.remove(c);
    }

    /**
     * Obtiene la lista completa de cartas en la mano del jugador.
     *
     * @return lista de cartas.
     */
    public List<Carta> getMano() {
        return mano;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Determina qué cartas de la mano pueden jugarse sin que la suma de la mesa supere 50.
     *
     * <p>Reglas aplicadas:</p>
     * <ul>
     *     <li>Los Ases pueden valer 1 o 10; si alguno de los dos valores es jugable, la carta es válida.</li>
     *     <li>Para el resto de cartas se usa su valor normal calculado con {@link Carta#valorJuego(int, int)}.</li>
     * </ul>
     *
     * @param sumaMesa suma actual de la mesa.
     * @return lista de cartas que el jugador puede jugar.
     */
    public List<Carta> cartasJugables(int sumaMesa) {
        List<Carta> jugables = new ArrayList<>();

        for (Carta c : mano) {

            // Caso especial: As (puede valer 1 o 10)
            if (c.getValor().equals("A")) {
                if (sumaMesa + 1 <= 50 || sumaMesa + 10 <= 50) {
                    jugables.add(c);
                }
            } else {
                // Otras cartas (2-8, 9, 10, J/Q/K)
                int valor = c.valorJuego(sumaMesa, 0);
                if (sumaMesa + valor <= 50) {
                    jugables.add(c);
                }
            }
        }

        return jugables;
    }
}
