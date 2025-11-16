package com.cincuentazo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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


    public int getSelectedAIPlayers() {
        return selectedAIPlayers;
    }
}