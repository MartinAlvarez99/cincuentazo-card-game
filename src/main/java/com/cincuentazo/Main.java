package com.cincuentazo;

import com.cincuentazo.model.Card;
import com.cincuentazo.model.Juego;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load player selection view (HU-1)
        Parent root = FXMLLoader.load(getClass().getResource("/com/cincuentazo/view/PlayerSelectionView.fxml"));
        primaryStage.setTitle("Cincuentazo - Card Game");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {

        //Prueba juego terminal
        /*Scanner sc = new Scanner(System.in);
        Juego juego = new Juego("Samuel");

        System.out.println("=== Bienvenido al Cincuentazo ===");
        System.out.println("Tu mano inicial:");

        while (!juego.jugadorPerdio()) {
            mostrarEstado(juego);

            System.out.print("\nElige el número de la carta que quieres jugar (1-4): ");
            int opcion = sc.nextInt() - 1;

            List<Card> mano = juego.getJugador().getMano();

            if (opcion < 0 || opcion >= mano.size()) {
                System.out.println("Opción no válida.");
                continue;
            }

            Card cartaElegida = mano.get(opcion);
            int valorAs = 0;

            // Si es As, el jugador elige si vale 1 o 10
            if (cartaElegida.getValor().equals("A")) {
                System.out.print("Elegiste un As. ¿Qué valor quieres darle? (1 o 10): ");
                valorAs = sc.nextInt();
                if (valorAs != 1 && valorAs != 10) {
                    System.out.println("Valor no válido, se usará 1 por defecto.");
                    valorAs = 1;
                }
            }

            boolean pudo = juego.jugarCarta(cartaElegida, valorAs);

            if (!pudo) {
                System.out.println("⚠️ No puedes jugar esa carta, pasarías de 50.");
            } else if (juego.jugadorPerdio()) {
                System.out.println("❌ Ya no tienes cartas que puedas jugar. ¡Perdiste!");
                break;
            }
        }

        sc.close();
    }

    private static void mostrarEstado(Juego juego) {
        System.out.println("\nSuma actual en la mesa: " + juego.getMesa().getSuma());
        System.out.println("Tus cartas:");

        List<Card> mano = juego.getJugador().getMano();
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + ". " + mano.get(i));
        }*/
    }

}
