package com.cincuentazo.model;

import java.util.List;

/**
 * Representa un jugador controlado por IA (bot).
 *
 * <p>Mantiene una mano de cartas y proporciona una estrategia simple
 * para elegir qué carta jugar según la suma actual de la mesa.</p>
 *
 * <p><b>Advertencia:</b> {@link #elegirCarta(int)} elimina la carta seleccionada de la mano
 * antes de retornarla; esto altera la colección durante la iteración.</p>
 */
public class Bot {

    /**
     * Nombre identificador del bot.
     */
    private String nombre;

    /**
     * Mano de cartas que posee el bot.
     */
    private List<Carta> mano;

    /**
     * Crea un bot con un nombre específico.
     *
     * @param nombre nombre asignado al bot.
     */
    public Bot(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Asigna la mano inicial del bot.
     *
     * @param mano lista de cartas entregadas al bot.
     */
    public void setMano(List<Carta> mano) {
        this.mano = mano;
    }

    /**
     * Retorna la mano actual del bot.
     *
     * @return lista de cartas en posesión del bot.
     */
    public List<Carta> getMano() {
        return mano;
    }

    /**
     * Obtiene el nombre del bot.
     *
     * @return nombre del bot.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Añade una carta a la mano del bot.
     *
     * @param c carta que se agregará a la mano.
     */
    public void agregarCarta(Carta c) {
        mano.add(c);
    }

    /**
     * Selecciona la carta que el bot jugará según la suma actual de la mesa.
     *
     * <p>La estrategia es lineal: recorre la mano y elige la primera carta cuyo valor
     * no haga que la suma total exceda 50.</p>
     *
     * <p>Si la carta elegida es un As, se considera su valor según
     * {@link Carta#valorJuego(int, int)}.</p>
     *
     * <p><b>Importante:</b> La carta seleccionada se elimina de la mano antes de ser retornada.</p>
     *
     * @param sumaMesa suma actual de la mesa.
     * @return la carta seleccionada, o {@code null} si no hay jugada posible.
     */
    public Carta elegirCarta(int sumaMesa) {

        for (Carta c : mano) {
            int valor = c.valorJuego(sumaMesa, c.getValor().equals("A") ? 1 : 0);

            if (sumaMesa + valor <= 50) {
                mano.remove(c);  // Se elimina la carta elegida de la mano
                return c;
            }
        }

        return null; // no hay jugada posible
    }
}
