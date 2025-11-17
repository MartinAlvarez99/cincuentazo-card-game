package com.cincuentazo.controller;

import com.cincuentazo.model.Bot;
import com.cincuentazo.model.Carta;
import com.cincuentazo.model.Juego;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Controlador de la vista principal de la partida (Game.fxml).
 *
 * <p>Gestiona la interfaz de usuario (mostrar cartas, actualizar suma, mensajes de turno),
 * procesa la interacción del jugador (seleccionar y tirar cartas) y coordina los turnos
 * de los bots (IA) mediante pausas y ejecución segura en el hilo de JavaFX.</p>
 *
 * <p>Protege contra reentradas (doble-click) y errores durante el flujo de juego,
 * usando bloqueo/desbloqueo de controles y Platform.runLater cuando es necesario.</p>
 *
 * @author Martin Alvarez, Samuel Vargas, Howard Sanchez
 */
public class GameController {

    /** Cantidad de bots seleccionada en la pantalla inicial. */
    private int cantidadBots = 0;

    /**
     * Establece cuántos bots debe cargar la partida.
     * Es llamado desde el controlador PlayerSelectionController.
     *
     * @param cantidad número de bots (1–3)
     */
    public void setCantidadBots(int cantidad) {
        this.cantidadBots = cantidad;
    }

    // ----------------------------
    // Elementos de la interfaz
    // ----------------------------

    @FXML private Button btnTirar;

    @FXML private ImageView carta1;
    @FXML private ImageView carta2;
    @FXML private ImageView carta3;
    @FXML private ImageView carta4;
    @FXML private ImageView cartaCentro;

    @FXML private Label suma;
    @FXML private Label turno;
    @FXML private Label seleccion;

    @FXML private HBox botBox1;
    @FXML private HBox botBox2;
    @FXML private HBox botBox3;

    @FXML private Label botName1;
    @FXML private Label botName2;
    @FXML private Label botName3;

    @FXML private ImageView botBack1;
    @FXML private ImageView botBack2;
    @FXML private ImageView botBack3;

    // ----------------------------
    // Lógica del juego
    // ----------------------------

    private Juego juego;
    private List<Bot> bots = new ArrayList<>();

    /** Índice de la carta seleccionada por el jugador (0–3). */
    private int cartaSeleccionada = -1;


    /**
     * Inicializa la vista una vez cargado el FXML.
     *
     * <p>Configura textos iniciales, listeners para clicks en cartas
     * y asigna la acción del botón "Tirar".</p>
     */
    @FXML
    public void initialize() {
        if (turno != null) turno.setWrapText(true);
        if (seleccion != null) seleccion.setWrapText(true);

        turno.setText("Elige una carta");
        btnTirar.setOnAction(e -> tirarCartaSeleccionada());

        carta1.setOnMouseClicked(evt -> seleccionarCarta(0));
        carta2.setOnMouseClicked(evt -> seleccionarCarta(1));
        carta3.setOnMouseClicked(evt -> seleccionarCarta(2));
        carta4.setOnMouseClicked(evt -> seleccionarCarta(3));
    }


    /**
     * Inicia la partida creando el objeto {@link Juego},
     * robando cartas iniciales para los bots y mostrando su interfaz.
     *
     * <p>También verifica si el jugador ya no tiene jugadas posibles al empezar.</p>
     */
    public void iniciarJuego() {

        // Mostrar u ocultar secciones según la cantidad de bots
        if (botBox1 != null) botBox1.setVisible(cantidadBots >= 1);
        if (botBox1 != null) botBox1.setManaged(cantidadBots >= 1);

        if (botBox2 != null) botBox2.setVisible(cantidadBots >= 2);
        if (botBox2 != null) botBox2.setManaged(cantidadBots >= 2);

        if (botBox3 != null) botBox3.setVisible(cantidadBots >= 3);
        if (botBox3 != null) botBox3.setManaged(cantidadBots >= 3);

        if (botName1 != null) { botName1.setVisible(cantidadBots >= 1); botName1.setManaged(cantidadBots >= 1); }
        if (botName2 != null) { botName2.setVisible(cantidadBots >= 2); botName2.setManaged(cantidadBots >= 2); }
        if (botName3 != null) { botName3.setVisible(cantidadBots >= 3); botName3.setManaged(cantidadBots >= 3); }

        juego = new Juego("Jugador 1");

        bots.clear();

        // Asignar 4 cartas iniciales a cada bot
        for (int i = 1; i <= cantidadBots; i++) {
            Bot bot = new Bot("Bot " + i);

            bot.setMano(new ArrayList<>());
            for (int c = 0; c < 4; c++) {
                bot.getMano().add(juego.robarCarta());
            }

            bots.add(bot);
        }

        actualizarInterfaz();
        checkPlayerHasMoves();
    }


    /**
     * Marca una carta como seleccionada por el jugador.
     *
     * @param index índice de la carta (0–3)
     */
    private void seleccionarCarta(int index) {
        cartaSeleccionada = index;
        seleccion.setText("Carta seleccionada: " + (index + 1));
    }


    /**
     * Lógica principal cuando el jugador presiona el botón "Tirar".
     *
     * <p>Valida la carta, bloquea los controles para evitar doble click,
     * actualiza el juego, muestra la carta en pantalla y da paso al turno de los bots.</p>
     */
    private void tirarCartaSeleccionada() {

        // Evitar doble-click rápido
        if (btnTirar.isDisable()) {
            System.out.println("[DEBUG] tirarCartaSeleccionada: botón ya deshabilitado, ignorando.");
            return;
        }

        // Si no seleccionó carta, no bloquear nada
        if (cartaSeleccionada == -1) {
            turno.setText("Selecciona una carta primero");
            return;
        }

        btnTirar.setDisable(true);

        try {
            Carta carta = juego.getJugador().getMano().get(cartaSeleccionada);

            int valorAs = 0;

            // Si es un As, pedir valor al usuario
            if (carta.getValor().equals("A")) {
                valorAs = elegirValorAs();
            }

            boolean ok;
            try {
                ok = juego.jugarCarta(carta, valorAs);
            } catch (Exception ex) {
                ex.printStackTrace();
                turno.setText("Error al jugar la carta (ver consola).");
                desbloquearJugador();
                return;
            }

            if (!ok) {
                turno.setText("No puedes jugar esa carta (supera 50)");
                cartaSeleccionada = -1;
                seleccion.setText("Carta seleccionada: -");
                actualizarInterfaz();

                if (juego.getJugador().cartasJugables(juego.getMesa().getSuma()).isEmpty()) {
                    mostrarAlertaFinDeJuego("❌ Perdiste, no tienes más jugadas posibles.");
                    return;
                }

                desbloquearJugador();
                return;
            }

            // Jugada válida → actualizar
            mostrarImagen(cartaCentro, carta);
            actualizarInterfaz();

            if (juego.jugadorPerdio()) {
                mostrarAlertaFinDeJuego("❌ Perdiste, no tienes más jugadas posibles.");
                return;
            }

            // Turno de bots
            turno.setText("Turno de los bots...");
            bloquearJugador();

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                try {
                    jugarBotRecursivo(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(this::desbloquearJugador);
                }
            });
            pause.play();

            cartaSeleccionada = -1;
        }
        finally {
            // El desbloqueo ocurre en los lugares correctos dentro del flujo
        }
    }


    /** Deshabilita todas las interacciones del jugador. */
    private void bloquearJugador() {
        btnTirar.setDisable(true);
        carta1.setDisable(true);
        carta2.setDisable(true);
        carta3.setDisable(true);
        carta4.setDisable(true);
    }

    /** Vuelve a habilitar las interacciones del jugador. */
    private void desbloquearJugador() {
        btnTirar.setDisable(false);
        carta1.setDisable(false);
        carta2.setDisable(false);
        carta3.setDisable(false);
        carta4.setDisable(false);
    }


    /**
     * Verifica si el jugador tiene al menos una carta jugable.
     * Si no, termina la partida.
     */
    private void checkPlayerHasMoves() {
        if (juego == null) return;
        boolean noHay = juego.getJugador().cartasJugables(juego.getMesa().getSuma()).isEmpty();
        if (noHay) {
            mostrarAlertaFinDeJuego("❌ Perdiste, no tienes más jugadas posibles.");
        }
    }


    /**
     * Controla recursivamente el turno de los bots.
     *
     * @param indexBot índice del bot actual en la lista
     */
    private void jugarBotRecursivo(int indexBot) {

        // Si todos los bots jugaron → vuelve el turno al jugador
        if (indexBot >= bots.size()) {
            turno.setText("Tu turno, elige una carta");
            desbloquearJugador();
            checkPlayerHasMoves();
            return;
        }

        Bot bot = bots.get(indexBot);
        turno.setText("Turno de: " + bot.getNombre());

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {
            try {
                Platform.runLater(() -> {
                    try {

                        Carta cartaBot = bot.elegirCarta(juego.getMesa().getSuma());

                        // Si el bot no puede jugar ninguna carta, se elimina
                        if (cartaBot == null) {
                            bots.remove(bot);

                            if (bots.isEmpty()) {
                                mostrarAlertaFinDeJuego("🎉 ¡Felicidades, ganaste! No quedan bots!");
                                return;
                            }

                            jugarBotRecursivo(indexBot);
                            return;
                        }

                        // Elegir valor del As para bot según convenga
                        int valorAs = 0;
                        if (cartaBot.getValor().equals("A")) {
                            valorAs = (juego.getMesa().getSuma() + 10 <= 50) ? 10 : 1;
                        }

                        // Intentar jugar
                        boolean jugado;
                        try {
                            jugado = juego.jugarCarta(cartaBot, valorAs);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            jugarBotRecursivo(indexBot + 1);
                            return;
                        }

                        if (!jugado) {
                            jugarBotRecursivo(indexBot + 1);
                            return;
                        }

                        mostrarImagen(cartaCentro, cartaBot);
                        actualizarInterfaz();

                        jugarBotRecursivo(indexBot + 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        jugarBotRecursivo(indexBot + 1);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                jugarBotRecursivo(indexBot + 1);
            }
        });

        delay.play();
    }


    /**
     * Actualiza la interfaz del jugador:
     * <ul>
     *     <li>Actualiza la suma en pantalla</li>
     *     <li>Muestra las cartas del jugador</li>
     * </ul>
     */
    private void actualizarInterfaz() {

        suma.setText("Suma: " + juego.getMesa().getSuma());

        mostrarCartaJugador(0, carta1);
        mostrarCartaJugador(1, carta2);
        mostrarCartaJugador(2, carta3);
        mostrarCartaJugador(3, carta4);
    }


    /**
     * Muestra una carta en uno de los slots del jugador.
     *
     * @param index índice de carta (0–3)
     * @param imgView vista donde cargar la imagen
     */
    private void mostrarCartaJugador(int index, ImageView imgView) {
        if (index >= juego.getJugador().getMano().size()) {
            imgView.setImage(null);
            return;
        }

        Carta c = juego.getJugador().getMano().get(index);
        mostrarImagen(imgView, c);
    }


    /**
     * Carga la imagen correspondiente a una carta en un ImageView.
     *
     * @param imgView ImageView destino
     * @param carta carta a mostrar
     */
    private void mostrarImagen(ImageView imgView, Carta carta) {
        if (carta == null) {
            imgView.setImage(null);
            return;
        }

        String simbolo = carta.getSimbolo();

        // Convertir símbolo a nombre de archivo
        if(carta.getSimbolo().equals("♠")){
            simbolo = " de picas";
        } else if(carta.getSimbolo().equals("♥")){
            simbolo = " de corazones";
        } else if(carta.getSimbolo().equals("♦")){
            simbolo = " de diamantes";
        } else if(carta.getSimbolo().equals("♣")){
            simbolo = " de treboles";
        }

        try {
            String fileName = carta.getValor() + simbolo + ".png";
            String path = "/com/cincuentazo/view/CardsPNG/" + fileName;

            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                throw new IllegalArgumentException("Recurso no encontrado: " + path);
            }
            Image img = new Image(is);
            imgView.setImage(img);

        } catch (Exception e) {
            System.out.println("No se encontró la imagen de la carta: " + carta + " -> " + e.getMessage());
            imgView.setImage(null);
        }
    }


    /**
     * Control del teclado:
     * <ul>
     *     <li>1–4: seleccionar carta</li>
     *     <li>Enter: tirar</li>
     * </ul>
     *
     * @param event evento KeyEvent capturado desde el FXML
     */
    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (btnTirar.isDisable()) return;

        switch (event.getCode()) {
            case DIGIT1: seleccionarCarta(0); break;
            case DIGIT2: seleccionarCarta(1); break;
            case DIGIT3: seleccionarCarta(2); break;
            case DIGIT4: seleccionarCarta(3); break;
            case ENTER:  tirarCartaSeleccionada(); break;
        }
    }


    /**
     * Muestra un diálogo para elegir si el As vale 1 o 10.
     *
     * @return valor del As elegido
     */
    private int elegirValorAs() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Elegir valor del AS");

        dialog.getDialogPane().setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-border-color: #3a3a3a;" +
                        "-fx-border-width: 2px;"
        );

        dialog.setHeaderText("Selecciona el valor del AS:");
        dialog.getDialogPane().lookup(".header-panel").setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: white;"
        );

        ButtonType btn1 = new ButtonType("Valor 1");
        ButtonType btn10 = new ButtonType("Valor 10");

        dialog.getDialogPane().getButtonTypes().addAll(btn1, btn10);

        dialog.getDialogPane().lookupButton(btn1).setStyle(
                "-fx-background-color: #2b2b2b;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;"
        );

        dialog.getDialogPane().lookupButton(btn10).setStyle(
                "-fx-background-color: #2b2b2b;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;"
        );

        Label content = new Label("¿Cuánto valdrá el As?");
        content.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == btn10) return 10;
            return 1;
        });

        return dialog.showAndWait().orElse(1);
    }


    /**
     * Reinicia el juego retornando a la pantalla de selección de jugadores.
     */
    private void reiniciarJuego() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/cincuentazo/view/PlayerSelectionView.fxml"));
            Stage stage = (Stage) btnTirar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Muestra un diálogo de fin de juego, con opción para reiniciar o cerrar.
     *
     * @param mensaje mensaje principal a mostrar
     */
    private void mostrarAlertaFinDeJuego(String mensaje) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin del Juego");
            alert.setHeaderText(mensaje);
            alert.setContentText("¿Qué deseas hacer?");

            ButtonType btnReiniciar = new ButtonType("Reiniciar");
            ButtonType btnCerrar = new ButtonType("Cerrar");

            alert.getButtonTypes().setAll(btnReiniciar, btnCerrar);

            alert.showAndWait().ifPresent(respuesta -> {
                if (respuesta == btnReiniciar) {
                    reiniciarJuego();
                } else {
                    Stage stage = (Stage) btnTirar.getScene().getWindow();
                    stage.close();
                }
            });
        });
    }
}
