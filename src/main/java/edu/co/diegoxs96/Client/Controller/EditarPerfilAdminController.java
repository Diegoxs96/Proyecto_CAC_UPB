package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditarPerfilAdminController {

    @FXML private TextField  fieldIdentificacion;
    @FXML private TextField  fieldCorreo;
    @FXML private TextField  fieldNombres;
    @FXML private TextField  fieldApellidos;
    @FXML private TextField  fieldContrasena;
    @FXML private TextField  fieldDireccion;
    @FXML private TextField  fieldEdad;
    @FXML private MenuButton menuPlan;
    @FXML private Label      labelMensaje;

    private int planSeleccionado = 0;

    @FXML private void handlePlanPremium()  { planSeleccionado = 1; menuPlan.setText("Premium"); }
    @FXML private void handlePlanEstandar() { planSeleccionado = 0; menuPlan.setText("Estándar"); }

    @FXML
    private void handleGuardar() {
        if (fieldNombres.getText().trim().isEmpty()) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("El nombre no puede estar vacío.");
            return;
        }
        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Perfil actualizado correctamente.");
    }

    @FXML
    private void handleCancelar() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/Admin/MenuAdmin.fxml"));
            Stage stage = (Stage) fieldNombres.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
