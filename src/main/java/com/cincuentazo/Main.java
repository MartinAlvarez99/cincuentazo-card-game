package com.cincuentazo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal que inicia la aplicación JavaFX.
 *
 * <p>Es responsable de cargar la primera vista del programa
 * ({@code PlayerSelectionView.fxml}) y configurar la ventana principal
 * con su tamaño inicial, título y propiedades visuales.</p>
 */
public class Main extends Application {

    /**
     * Método de entrada de JavaFX.
     *
     * <p>Carga la vista inicial donde se selecciona la cantidad de jugadores IA
     * y la coloca dentro de una escena para ser mostrada en pantalla.</p>
     *
     * @param primaryStage la ventana principal proporcionada por el sistema.
     * @throws Exception si no se puede cargar el archivo FXML inicial.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/cincuentazo/view/PlayerSelectionView.fxml")
        );

        primaryStage.setTitle("Cincuentazo - Card Game");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setResizable(false); // Ventana fija, no redimensionable
        primaryStage.show();
    }

    /**
     * Método estándar de ejecución de aplicaciones Java.
     *
     * <p>Llama a {@link Application#launch} para iniciar el ciclo de vida de JavaFX.</p>
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
