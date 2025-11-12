package com.cincuentazo.model;

public class Juego {

    private Mazo mazo;
    private Mesa mesa;
    private Player player;
    private boolean jugadorPerdio;

    public Juego(String nombre) {
        mazo = new Mazo();
        mesa = new Mesa();
        player = new Player(nombre);
        jugadorPerdio = false;

        // repartir 4 cartas
        for (int i = 0; i < 4; i++) {
            player.agregarCarta(mazo.sacarCarta());
        }

        // carta inicial en la mesa
        mesa.ponerCarta(mazo.sacarCarta(), 0);
    }

    // valorAs = 0 para cartas normales, o 1/10 cuando el jugador elige
    public boolean jugarCarta(Card carta, int valorAs) {
        if (jugadorPerdio) {
            return false; // si ya perdió, no puede jugar
        }

        int valorCarta = carta.valorJuego(mesa.getSuma(), valorAs);
        int nuevaSuma = mesa.getSuma() + valorCarta;

        // si pasa de 50, no se puede jugar esa carta
        if (nuevaSuma > 50) {
            return false;
        }

        // jugar carta y actualizar
        mesa.ponerCarta(carta, valorCarta);
        player.quitarCarta(carta);

        // si el mazo no está vacío, se roba otra
        if (!mazo.estaVacio()) {
            player.agregarCarta(mazo.sacarCarta());
        }

        // verificar si el jugador puede seguir jugando
        verificarPosibleJugada();

        return true;
    }

    // Revisa si el jugador tiene al menos una carta que pueda jugar
    private void verificarPosibleJugada() {
        if (player.cartasJugables(mesa.getSuma()).isEmpty()) {
            jugadorPerdio = true; // si no tiene jugadas posibles, pierde
        }
    }

    public boolean jugadorPerdio() {
        return jugadorPerdio;
    }

    public Player getJugador() {
        return player;
    }

    public Mesa getMesa() {
        return mesa;
    }
}
