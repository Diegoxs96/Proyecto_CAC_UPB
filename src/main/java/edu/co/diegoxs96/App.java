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
            // Carga la vista de login del compañero
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/InicioDeSesion/login.fxml"));
            stage.setTitle("CAC-UPB — Cliente");
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            // Servidor
            edu.co.diegoxs96.Server.Factory.ServerFactory.create().show(stage);
        }
    }

    public static void main(String[] args) { launch(args); }
}
