package com.cincuentazo.controller;

import com.cincuentazo.model.Bot;
import com.cincuentazo.model.Carta;
import com.cincuentazo.model.Juego;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private int cantidadBots = 0;
    public void setCantidadBots(int cantidad) {
        this.cantidadBots = cantidad;
    }

    @FXML private Button btnTirar;
    @FXML private ImageView carta1;
    @FXML private ImageView carta2;
    @FXML private ImageView carta3;
    @FXML private ImageView carta4;
    @FXML private ImageView cartaCentro;
    @FXML private Label suma;
    @FXML private Label turno;
    @FXML private Label seleccion;

    private Juego juego;
    private List<Bot> bots = new ArrayList<>();
    private int cartaSeleccionada = -1;

    @FXML
    public void initialize() {
        turno.setText("Elige una carta");
        btnTirar.setOnAction(e -> tirarCartaSeleccionada());

        carta1.setOnMouseClicked(evt -> seleccionarCarta(0));
        carta2.setOnMouseClicked(evt -> seleccionarCarta(1));
        carta3.setOnMouseClicked(evt -> seleccionarCarta(2));
        carta4.setOnMouseClicked(evt -> seleccionarCarta(3));
    }

    public void iniciarJuego() {
        juego = new Juego("Jugador 1");

        bots.clear();

        for (int i = 1; i <= cantidadBots; i++) {
            Bot bot = new Bot("Bot " + i);


            bot.setMano(new ArrayList<>());
            for (int c = 0; c < 4; c++) {
                bot.getMano().add(juego.robarCarta());
            }

            bots.add(bot);
        }

        actualizarInterfaz();
    }

    private void seleccionarCarta(int index) {
        cartaSeleccionada = index;
        seleccion.setText("Carta seleccionada: " + (index + 1));
    }

    private void tirarCartaSeleccionada() {
        if (cartaSeleccionada == -1) {
            turno.setText("Selecciona una carta primero");
            return;
        }

        Carta carta = juego.getJugador().getMano().get(cartaSeleccionada);

        int valorAs = 0;
        if (carta.getValor().equals("A")) {
            valorAs = elegirValorAs();
        }

        boolean ok = juego.jugarCarta(carta, valorAs);

        if (!ok) {
            turno.setText("No puedes jugar esa carta (supera 50)");
            return;
        }

        mostrarImagen(cartaCentro, carta);
        actualizarInterfaz();

        if (juego.jugadorPerdio()) {
            mostrarAlertaFinDeJuego("❌ Perdiste, no tienes más jugadas posibles.");
            return;
        }


        turno.setText("Turno de los bots...");
        bloquearJugador();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> jugarBotRecursivo(0));
        pause.play();

        cartaSeleccionada = -1;
    }

    private void bloquearJugador() {
        btnTirar.setDisable(true);

        carta1.setDisable(true);
        carta2.setDisable(true);
        carta3.setDisable(true);
        carta4.setDisable(true);
    }

    private void desbloquearJugador() {
        btnTirar.setDisable(false);

        carta1.setDisable(false);
        carta2.setDisable(false);
        carta3.setDisable(false);
        carta4.setDisable(false);
    }



    private void jugarBotRecursivo(int indexBot) {

        if (indexBot >= bots.size()) {
            turno.setText("Tu turno, elige una carta");
            desbloquearJugador();
            return;
        }

        Bot bot = bots.get(indexBot);
        turno.setText("Turno de: " + bot.getNombre());

        PauseTransition delay = new PauseTransition(Duration.seconds(2));

        delay.setOnFinished(event -> {

            Carta cartaBot = bot.elegirCarta(juego.getMesa().getSuma());

            if (cartaBot == null) {
                bots.remove(bot);

                if (bots.isEmpty()) {
                    mostrarAlertaFinDeJuego("🎉 ¡Felicidades, ganaste! No quedan bots!");
                    return;
                }

                jugarBotRecursivo(indexBot); // no incrementa porque un bot se eliminó
                return;
            }

            int valorAs = 0;
            if (cartaBot.getValor().equals("A")) {
                valorAs = (juego.getMesa().getSuma() + 10 <= 50) ? 10 : 1;
            }

            juego.jugarCarta(cartaBot, valorAs);

            mostrarImagen(cartaCentro, cartaBot);
            actualizarInterfaz();

            jugarBotRecursivo(indexBot + 1);
        });

        delay.play();
    }


    private void actualizarInterfaz() {

        suma.setText("Suma: " + juego.getMesa().getSuma());

        mostrarCartaJugador(0, carta1);
        mostrarCartaJugador(1, carta2);
        mostrarCartaJugador(2, carta3);
        mostrarCartaJugador(3, carta4);
    }

    private void mostrarCartaJugador(int index, ImageView imgView) {
        if (index >= juego.getJugador().getMano().size()) {
            imgView.setImage(null);
            return;
        }

        Carta c = juego.getJugador().getMano().get(index);
        mostrarImagen(imgView, c);
    }

    private void mostrarImagen(ImageView imgView, Carta carta) {
        String simbolo = carta.getSimbolo();

        if(carta.getSimbolo().equals("♠")){
            simbolo = " de picas";
        } else if(carta.getSimbolo().equals("♥")){
            simbolo = " de corazones";
        }else if(carta.getSimbolo().equals("♦")){
            simbolo = " de diamantes";
        }else if(carta.getSimbolo().equals("♣")){
            simbolo = " de treboles";
        }

        try {

            String fileName = carta.getValor() + simbolo + ".png";
            String path = "/com/cincuentazo/CardsPNG/" + fileName;

            Image img = new Image(getClass().getResource(path).toString());
            imgView.setImage(img);

        } catch (Exception e) {
            System.out.println("No se encontró la imagen de la carta: " + carta);
            imgView.setImage(null);
        }
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (btnTirar.isDisable()) return;
        switch (event.getCode()) {
            case DIGIT1:
                seleccionarCarta(0);
                break;

            case DIGIT2:
                seleccionarCarta(1);
                break;

            case DIGIT3:
                seleccionarCarta(2);
                break;

            case DIGIT4:
                seleccionarCarta(3);
                break;

            case ENTER:
                tirarCartaSeleccionada();
                break;
        }
    }

    private int elegirValorAs() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Elegir valor del AS");

        // Fondo oscuro
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

        // Botones personalizados
        ButtonType btn1 = new ButtonType("Valor 1");
        ButtonType btn10 = new ButtonType("Valor 10");

        dialog.getDialogPane().getButtonTypes().addAll(btn1, btn10);

        // Personalizar botones (redondeados y oscuros)
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

        // Contenido
        Label content = new Label("¿Cuánto valdrá el As?");
        content.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == btn10) return 10;
            return 1;
        });

        return dialog.showAndWait().orElse(1);
    }

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

    private void mostrarAlertaFinDeJuego(String mensaje) {

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
                // cerrar ventana
                Stage stage = (Stage) btnTirar.getScene().getWindow();
                stage.close();
            }
        });
    }
}
