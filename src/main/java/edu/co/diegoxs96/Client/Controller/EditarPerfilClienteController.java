package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.Naming;

public class EditarPerfilClienteController {

    @FXML private TextField fieldCorreo;
    @FXML private TextField fieldContrasena;
    @FXML private TextField fieldDireccion;
    @FXML private Label     labelMensaje;

    private TicketInterface service;

    @FXML
    private void initialize() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[EDITAR PERFIL] Sin conexión al servidor.");
        }
    }

    @FXML
    private void handleGuardar() {
        String contrasena = fieldContrasena.getText().trim();
        String direccion  = fieldDireccion.getText().trim();
        String correo     = fieldCorreo.getText().trim();

        if (contrasena.isEmpty() && direccion.isEmpty() && correo.isEmpty()) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Ingresa al menos un campo para actualizar.");
            return;
        }
        if (service == null) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Sin conexión al servidor.");
            return;
        }

        int clienteId = Sesion.getInstance().getClienteId();
        if (clienteId == -1) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Sesión no iniciada.");
            return;
        }

        try {
            boolean ok = service.actualizarPerfil(clienteId, correo, contrasena, direccion);
            if (ok) {
                labelMensaje.setStyle("-fx-text-fill: green;");
                labelMensaje.setText("Perfil actualizado correctamente.");
            } else {
                labelMensaje.setStyle("-fx-text-fill: red;");
                labelMensaje.setText("No se encontró el cliente.");
            }
        } catch (Exception e) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        Sesion.getInstance().cerrar();
        navegar("/edu/co/diegoxs96/views/InicioDeSesion/login.fxml");
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()   { System.out.println("[EDITAR PERFIL] Ya estás aquí"); }
    @FXML private void handleSolicitarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/SolicitarCita.fxml"); }
    @FXML private void handleVerCita()        { navegar("/edu/co/diegoxs96/views/Cliente/VerCita.fxml"); }
    @FXML private void handleModificarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/ModificarCita.fxml"); }
    @FXML private void handleConsultarTurno() { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()      { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCliente.fxml"); }
    @FXML private void handleCancelarCita()   { System.out.println("[EDITAR PERFIL] Cancelar cita — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) fieldCorreo.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}