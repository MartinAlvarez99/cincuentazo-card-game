package com.cincuentazo.model;

/**
 * Clase principal del dominio que contiene el estado del juego.
 *
 * <p>Encapsula el {@link Mazo}, la {@link Mesa} y el {@link Jugador}. Se encarga de repartir
 * las cartas iniciales, colocar la carta inicial en mesa y aplicar la lógica al jugar cartas:
 * validar que la suma no exceda 50, actualizar la mesa, robar del mazo y comprobar si el jugador
 * perdió (sin jugadas posibles).</p>
 */
public class Juego {

    /** Mazo principal desde el cual se roban las cartas. */
    private Mazo mazo;

    /** Representa la mesa del juego, incluyendo la suma actual y cartas jugadas. */
    private Mesa mesa;

    /** Jugador principal que participa en la partida. */
    private Jugador jugador;

    /** Indica si el jugador ha perdido por no tener más jugadas posibles. */
    private boolean jugadorPerdio;

    /**
     * Crea un nuevo juego: inicializa el mazo, la mesa, el jugador y reparte las cartas.
     *
     * <p>Acciones realizadas:</p>
     * <ul>
     *   <li>Crea un mazo nuevo y una mesa vacía.</li>
     *   <li>Inicializa al jugador con el nombre ingresado.</li>
     *   <li>Reparte 4 cartas al jugador.</li>
     *   <li>Coloca una carta inicial en la mesa.</li>
     * </ul>
     *
     * @param nombre nombre del jugador.
     */
    public Juego(String nombre) {
        mazo = new Mazo();
        mesa = new Mesa();
        jugador = new Jugador(nombre);
        jugadorPerdio = false;

        // Repartir 4 cartas al jugador
        for (int i = 0; i < 4; i++) {
            jugador.agregarCarta(mazo.sacarCarta());
        }

        // Carta inicial en la mesa
        mesa.ponerCarta(mazo.sacarCarta(), 0);
    }

    /**
     * Permite al jugador jugar una carta sobre la mesa.
     *
     * <p>Validaciones y acciones:</p>
     * <ul>
     *     <li>Si el jugador ya perdió, no puede jugar.</li>
     *     <li>Calcula el valor real aportado por la carta acorde a la suma actual.</li>
     *     <li>Verifica que la nueva suma no supere 50.</li>
     *     <li>Si es válida, coloca la carta en la mesa.</li>
     *     <li>La remueve de la mano y roba una nueva carta (si el mazo tiene).</li>
     *     <li>Se verifica si todavía queda alguna jugada posible.</li>
     * </ul>
     *
     * @param carta carta seleccionada por el jugador.
     * @param valorAs valor elegido en caso de que la carta sea un As (1 o 10).
     * @return {@code true} si se pudo jugar la carta, {@code false} si la jugada no es válida.
     */
    public boolean jugarCarta(Carta carta, int valorAs) {
        if (jugadorPerdio) {
            return false; // no puede jugar si ya perdió
        }

        int valorCarta = carta.valorJuego(mesa.getSuma(), valorAs);
        int nuevaSuma = mesa.getSuma() + valorCarta;

        // No puede superar 50
        if (nuevaSuma > 50) {
            return false;
        }

        // Actualizar mesa y jugador
        mesa.ponerCarta(carta, valorCarta);
        jugador.quitarCarta(carta);

        // Robar una carta del mazo si aún hay
        if (!mazo.estaVacio()) {
            jugador.agregarCarta(mazo.sacarCarta());
        }

        // Comprobar si el jugador aún tiene jugadas válidas
        verificarPosibleJugada();

        return true;
    }

    /** Extrae y devuelve la siguiente carta del mazo. */
    public Carta robarCarta() {
        return mazo.sacarCarta();
    }

    /** Obtiene el mazo actual. */
    public Mazo getMazo() {
        return mazo;
    }

    /**
     * Revisa si el jugador tiene al menos una carta jugable con la suma actual de la mesa.
     * Si no tiene jugadas posibles, el jugador pierde.
     */
    private void verificarPosibleJugada() {
        if (jugador.cartasJugables(mesa.getSuma()).isEmpty()) {
            jugadorPerdio = true;
        }
    }

    /** Verifica si el jugador perdió por no tener jugadas posibles. */
    public boolean jugadorPerdio() {
        return jugadorPerdio;
    }

    /** Obtiene al jugador de la partida. */
    public Jugador getJugador() {
        return jugador;
    }

    /** Obtiene la mesa del juego. */
    public Mesa getMesa() {
        return mesa;
    }
}
