package com.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/cincuentazo/view/PlayerSelectionView.fxml"));
        primaryStage.setTitle("Cincuentazo - Card Game");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false); // Ventana fija, no redimensionable
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
