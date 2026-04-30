package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.Naming;

public class LoginViewController {

    @FXML private TextField     fieldIdentificacion;
    @FXML private PasswordField fieldContrasena;
    @FXML private Label         labelError;

    private TicketInterface service;

    @FXML
    private void initialize() {
        conectarServidor();
    }

    @FXML
    private void handleLogin() {
        String id       = fieldIdentificacion.getText().trim();
        String password = fieldContrasena.getText().trim();

        if (id.isEmpty() || password.isEmpty()) {
            labelError.setText("Completa todos los campos.");
            return;
        }

        // Validación básica — se expande cuando el servidor exponga login vía RMI
        if (service == null) {
            labelError.setText("Sin conexión al servidor.");
            return;
        }

        // Por ahora acepta cualquier id numérico con contraseña no vacía
        // Tu compañero conectará esto a LoginController cuando se exponga por RMI
        try {
            Integer.parseInt(id);
            irAlMenu();
        } catch (NumberFormatException e) {
            labelError.setText("El ID debe ser numérico.");
        }
    }

    @FXML
    private void handleIrARegistro() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/InicioDeSesion/Registrer.fxml"));
            Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            labelError.setText("Error cargando registro: " + e.getMessage());
        }
    }

    private void irAlMenu() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/Menu/Menu.fxml"));
            Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            labelError.setText("Error cargando menú: " + e.getMessage());
        }
    }

    private void conectarServidor() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            // El servidor puede no estar listo aún — se reintenta al hacer login
            System.out.println("[LOGIN] Servidor no disponible aún.");
        }
    }
}
