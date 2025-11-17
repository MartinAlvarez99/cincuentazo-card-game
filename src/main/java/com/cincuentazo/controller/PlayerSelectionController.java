package com.cincuentazo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador de la vista de selección de jugadores IA (PlayerSelectionView.fxml).
 *
 * <p>Permite elegir cuántos bots habrá en la partida y al iniciar la escena de juego
 * pasa esa configuración al {@code GameController} para iniciar la partida.</p>
 *
 */
public class PlayerSelectionController {

    /**
     * RadioButton para seleccionar 1 jugador IA.
     */
    @FXML private RadioButton oneAIPlayer;

    /**
     * RadioButton para seleccionar 2 jugadores IA.
     */
    @FXML private RadioButton twoAIPlayers;

    /**
     * RadioButton para seleccionar 3 jugadores IA.
     */
    @FXML private RadioButton threeAIPlayers;

    /**
     * Botón que inicia la partida con la configuración seleccionada.
     */
    @FXML private Button startButton;

    /**
     * Número de bots seleccionado (valor por defecto: 1).
     */
    private int selectedAIPlayers = 1;

    /**
     * Inicializa los controles FXML asociados a la vista.
     *
     * <p>Configura el {@link ToggleGroup} para los RadioButtons, establece la selección
     * por defecto (1 IA) y añade listeners que mantienen el valor de
     * {@link #selectedAIPlayers} sincronizado con la interfaz.</p>
     *
     * <p>Este método se ejecuta automáticamente por JavaFX tras cargar el FXML.</p>
     */
    @FXML
    public void initialize() {
        // Configurar grupo de radio buttons
        ToggleGroup aiPlayersGroup = new ToggleGroup();
        oneAIPlayer.setToggleGroup(aiPlayersGroup);
        twoAIPlayers.setToggleGroup(aiPlayersGroup);
        threeAIPlayers.setToggleGroup(aiPlayersGroup);

        // Seleccionar por defecto 1 jugador IA
        oneAIPlayer.setSelected(true);

        // Listeners para actualizar la selección
        oneAIPlayer.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) selectedAIPlayers = 1;
        });

        twoAIPlayers.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) selectedAIPlayers = 2;
        });

        threeAIPlayers.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) selectedAIPlayers = 3;
        });
    }

    /**
     * Manejador del botón "Iniciar Juego".
     *
     * <p>Carga la vista del juego ({@code Game.fxml}), recupera su controlador,
     * le pasa la cantidad de bots seleccionada y llama a {@code iniciarJuego()}
     * antes de mostrar la nueva escena.</p>
     *
     * @param event evento de acción disparado por el botón
     */
    @FXML
    private void handleStartGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cincuentazo/view/Game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.setCantidadBots(selectedAIPlayers);
            controller.iniciarJuego();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la cantidad de bots actualmente seleccionada en la vista.
     *
     * @return número de jugadores IA seleccionados (1–3)
     */
    public int getSelectedAIPlayers() {
        return selectedAIPlayers;
    }
}
