package edu.co.diegoxs96;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String mode = getParameters().getRaw().isEmpty()
                ? "server"
                : getParameters().getRaw().get(0);

        if (mode.equals("client")) {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/InicioDeSesion/login.fxml"));
            stage.setTitle("CAC-UPB — Cliente");
            stage.setScene(new Scene(root));
            stage.show();
        } else if (mode.equals("monitor")) {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/monitor/PantallaTurno.fxml"));
            stage.setTitle("CAC-UPB — Monitor");
            stage.setScene(new Scene(root));
            stage.show();
        } else if (mode.equals("maquina")) {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/maquina/MaquinaEntrada.fxml"));
            stage.setTitle("CAC-UPB — Máquina de Entrada");
            stage.setScene(new Scene(root));
            stage.show();
        } else if (mode.equals("banco")) {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/banco/VerCitaBanco.fxml"));
            stage.setTitle("CAC-UPB — Operador Banco");
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // Carga la vista del Server
            edu.co.diegoxs96.Server.Factory.ServerFactory.create().show(stage);
        }
    }

    public static void main(String[] args) { launch(args); }
}