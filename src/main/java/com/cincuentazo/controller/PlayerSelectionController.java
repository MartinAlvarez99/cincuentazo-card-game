package com.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class PlayerSelectionController {

    @FXML private RadioButton oneAIPlayer;
    @FXML private RadioButton twoAIPlayers;
    @FXML private RadioButton threeAIPlayers;
    @FXML private Button startButton;

    private int selectedAIPlayers = 1;

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

    @FXML
    private void handleStartGame() {
        // Mostrar confirmación
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Iniciando Juego");
        alert.setHeaderText("Configuración del juego");
        alert.setContentText("El juego iniciará con:\n" +
                "- 1 jugador humano\n" +
                "- " + selectedAIPlayers + " jugadores IA\n" +
                "- Total: " + (selectedAIPlayers + 1) + " jugadores");
        alert.showAndWait();

        // TODO: Aquí irá la navegación a la pantalla del juego
        System.out.println("Iniciando juego con " + selectedAIPlayers + " jugadores IA");
    }

    public int getSelectedAIPlayers() {
        return selectedAIPlayers;
    }
}