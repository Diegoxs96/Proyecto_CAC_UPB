package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Server.Controller.GestorClientes;
import edu.co.diegoxs96.Server.Model.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistrerController {

    @FXML private TextField  fieldIdentificacion;
    @FXML private TextField  fieldCorreo;
    @FXML private TextField  fieldNombres;
    @FXML private TextField  fieldApellidos;
    @FXML private TextField  fieldEdad;
    @FXML private TextField  fieldContrasena;
    @FXML private TextField  fieldDireccion;
    @FXML private MenuButton menuTipo;
    @FXML private Label      labelError;

    private int tipoSeleccionado = Cliente.TIPO_ESTANDAR;

    @FXML
    private void handleTipoEstandar() {
        tipoSeleccionado = Cliente.TIPO_ESTANDAR;
        menuTipo.setText("Estándar");
    }

    @FXML
    private void handleTipoPremium() {
        tipoSeleccionado = Cliente.TIPO_PREMIUM;
        menuTipo.setText("Premium");
    }

    @FXML
    private void handleRegistrar() {
        String id        = fieldIdentificacion.getText().trim();
        String nombres   = fieldNombres.getText().trim();
        String apellidos = fieldApellidos.getText().trim();
        String edadStr   = fieldEdad.getText().trim();
        String contrasena= fieldContrasena.getText().trim();
        String direccion = fieldDireccion.getText().trim();

        // Validaciones
        if (id.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || edadStr.isEmpty() || contrasena.isEmpty() || direccion.isEmpty()) {
            labelError.setText("Todos los campos son obligatorios.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad <= 0 || edad > 120) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            labelError.setText("Ingresa una edad válida.");
            return;
        }

        // Registro — GestorClientes vive en el servidor, aquí se llama vía RMI
        // Por ahora se conecta directamente hasta que se exponga el método por RMI
        GestorClientes gestorClientes = new GestorClientes();
        Cliente cliente = gestorClientes.registrarCliente(
                id, nombres, apellidos, contrasena, edad, direccion, tipoSeleccionado);

        if (cliente == null) {
            labelError.setText("Ya existe un cliente con ese número de identificación.");
            return;
        }

        labelError.setStyle("-fx-text-fill: green;");
        labelError.setText("Cuenta creada exitosamente. Redirigiendo...");

        // Ir al login después del registro
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(
                javafx.util.Duration.seconds(1.5));
        pause.setOnFinished(e -> handleIrALogin());
        pause.play();
    }

    @FXML
    private void handleCancelar() {
        handleIrALogin();
    }

    @FXML
    private void handleIrALogin() {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/edu/co/diegoxs96/views/InicioDeSesion/login.fxml"));
            Stage stage = (Stage) fieldNombres.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            labelError.setText("Error cargando login: " + e.getMessage());
        }
    }
}
