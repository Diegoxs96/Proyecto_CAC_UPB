package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarPerfilClienteController {

    @FXML private TextField fieldCorreo;
    @FXML private TextField fieldContrasena;
    @FXML private TextField fieldDireccion;
    @FXML private Label     labelMensaje;

    @FXML
    private void handleGuardar() {
        if (fieldContrasena.getText().trim().isEmpty()) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("La contraseña no puede estar vacía.");
            return;
        }
        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Perfil actualizado correctamente.");
    }

    @FXML
    private void handleCancelar() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/Cliente/Menu.fxml"));
            Stage stage = (Stage) fieldCorreo.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
