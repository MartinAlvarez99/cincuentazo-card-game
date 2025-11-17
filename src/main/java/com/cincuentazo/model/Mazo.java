package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo (baraja) del juego.
 *
 * <p>Construye un mazo estándar compuesto por 52 cartas (valores 2–A, en los cuatro
 * símbolos ♠ ♥ ♦ ♣), lo mezcla de forma aleatoria y permite extraer cartas de una en una.
 * También provee utilidades para consultar si el mazo está vacío, barajarlo nuevamente
 * y conocer cuántas cartas quedan.</p>
 */
public class Mazo {

    /** Lista que contiene todas las cartas restantes en el mazo. */
    private List<Carta> cartas;

    /**
     * Crea un mazo estándar de 52 cartas y lo mezcla aleatoriamente.
     *
     * <p>El mazo incluye cartas desde el "2" hasta el "A" para cada uno
     * de los símbolos disponibles (♠, ♥, ♦, ♣).</p>
     */
    public Mazo() {
        cartas = new ArrayList<>();

        // Valores y símbolos del mazo estándar
        String[] valores = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] simbolos = {"♠","♥","♦","♣"};

        // Crear todas las combinaciones de cartas
        for (String s : simbolos) {
            for (String v : valores) {
                cartas.add(new Carta(v, s));
            }
        }

        // Mezclar las cartas para un orden aleatorio inicial
        Collections.shuffle(cartas);
    }

    /**
     * Extrae y retorna la carta superior del mazo (posición 0).
     *
     * @return la carta extraída, o {@code null} si el mazo está vacío.
     */
    public Carta sacarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    /**
     * Indica si el mazo ya no tiene cartas disponibles.
     *
     * @return {@code true} si el mazo está vacío; de lo contrario {@code false}.
     */
    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    /**
     * Mezcla aleatoriamente las cartas restantes en el mazo.
     */
    public void barajar() {
        Collections.shuffle(cartas);
    }

    /**
     * Obtiene el número de cartas que quedan en el mazo.
     *
     * @return cantidad de cartas restantes.
     */
    public int cartasRestantes() {
        return cartas.size();
    }

    /**
     * Devuelve la lista completa de cartas del mazo.
     *
     * <p>Nota: Se retorna la lista interna directamente; si deseas evitar modificaciones
     * externas, podría devolverse una copia con {@code new ArrayList<>(cartas)}.</p>
     *
     * @return lista de cartas que componen el mazo.
     */
    public List<Carta> getCartas() {
        return cartas;
    }
}
