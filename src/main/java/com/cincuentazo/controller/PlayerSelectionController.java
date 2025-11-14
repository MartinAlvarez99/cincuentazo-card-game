package com.cincuentazo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class PlayerSelectionController {

    @FXML private RadioButton oneAIPlayer;
    @FXML private RadioButton twoAIPlayers;
    @FXML private RadioButton threeAIPlayers;
    @FXML private Button startButton;

    private int selectedAIPlayers = 1;

    @FXML
    public void initialize() {
        ToggleGroup aiPlayersGroup = new ToggleGroup();
        oneAIPlayer.setToggleGroup(aiPlayersGroup);
        twoAIPlayers.setToggleGroup(aiPlayersGroup);
        threeAIPlayers.setToggleGroup(aiPlayersGroup);

        oneAIPlayer.setSelected(true);

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
    private void handleStartGame(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Iniciando Juego");
        alert.setHeaderText("Configuración del juego");
        alert.setContentText("El juego iniciará con:\n" +
                "- 1 jugador humano\n" +
                "- " + selectedAIPlayers + " jugadores IA\n" +
                "- Total: " + (selectedAIPlayers + 1) + " jugadores");
        alert.showAndWait();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/cincuentazo/view/Game.fxml")); // Ajusta esta ruta si es necesario
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public int getSelectedAIPlayers() {
        return selectedAIPlayers;
    }
}