package com.cincuentazo.model;

/**
 * Modelo que representa una carta del juego.
 *
 * <p>Cada carta contiene un {@code valor} (por ejemplo "A", "2", "J", ...)
 * y un {@code simbolo} (por ejemplo "♠", "♥", "♦", "♣").
 * Proporciona utilidades para obtener el valor que aporta al juego según las reglas
 * especiales del cincuentazo (J/Q/K = -10, 9 = 0, A = 1 u 11, etc.).</p>
 */
public class Carta {

    /**
     * Representación textual del valor de la carta (A, 2–10, J, Q, K).
     */
    private String valor;

    /**
     * Símbolo del palo de la carta (♠, ♥, ♦, ♣).
     */
    private String simbolo;

    /**
     * Construye una carta con un valor y un símbolo de palo.
     *
     * @param valor   valor de la carta (A, 2–10, J, Q, K)
     * @param simbolo palo o símbolo asociado (♠, ♥, ♦, ♣)
     */
    public Carta(String valor, String simbolo) {
        this.valor = valor;
        this.simbolo = simbolo;
    }

    /**
     * Obtiene el valor de la carta (tal cual aparece).
     *
     * @return texto del valor (por ejemplo "A" o "7")
     */
    public String getValor() {
        return valor;
    }

    /**
     * Retorna el símbolo o palo de la carta.
     *
     * @return símbolo Unicode del palo (♠, ♥, ♦, ♣)
     */
    public String getSimbolo() {
        return simbolo;
    }

    /**
     * Obtiene el valor base de la carta según reglas simplificadas.
     *
     * <p>Este método es utilizado por la IA para analizar jugadas,
     * por lo que no considera variaciones como el As flexible.</p>
     *
     * <ul>
     *   <li>J, Q, K → -10</li>
     *   <li>A → 11</li>
     *   <li>9 → 0</li>
     *   <li>2-10 → su valor numérico</li>
     * </ul>
     *
     * @return valor base de la carta
     */
    public int getValorBase() {
        if (valor.equals("J") || valor.equals("Q") || valor.equals("K")) {
            return -10;
        }

        if (valor.equals("A")) {
            return 11;
        }

        if (valor.equals("9")) {
            return 0;
        }

        return Integer.parseInt(valor);
    }

    /**
     * Calcula el valor real que esta carta aporta al juego,
     * considerando la suma actual de la mesa y reglas especiales.
     *
     * <ul>
     *   <li>J, Q, K → -10</li>
     *   <li>9 → 0</li>
     *   <li>A → depende del parámetro {@code valorAs}</li>
     *   <li>2-10 → valor normal</li>
     * </ul>
     *
     * @param sumaMesa suma actual que hay en la mesa
     * @param valorAs valor elegido para el As (1 o 11)
     * @return el valor que la carta aportaría en el contexto actual
     */
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

        // Cartas 2-8 y 10
        return Integer.parseInt(valor);
    }

    /**
     * Representación textual de la carta (por ejemplo "A♠").
     *
     * @return cadena con valor + símbolo
     */
    @Override
    public String toString() {
        return valor + simbolo;
    }
}
