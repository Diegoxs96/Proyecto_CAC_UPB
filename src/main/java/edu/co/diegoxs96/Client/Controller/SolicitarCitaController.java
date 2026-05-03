package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.Naming;

public class SolicitarCitaController {

    @FXML private MenuButton menuTipoCita;
    @FXML private MenuButton menuHora;
    @FXML private TextField  fieldFecha;
    @FXML private TextField  fieldMotivo;
    @FXML private Label      labelMensaje;

    private int    tipoCita = -1;
    private String horaSeleccionada = null;
    private TicketInterface service;

    @FXML
    private void initialize() {
        conectar();
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[SOLICITAR CITA] Sin conexión al servidor.");
        }
    }

    // ── Tipo de cita ─────────────────────────────────────────────────────────

    @FXML private void handleTipoAsesoria()   { tipoCita = 2; menuTipoCita.setText("Asesoría"); }
    @FXML private void handleTipoReclamo()    { tipoCita = 0; menuTipoCita.setText("Reclamo"); }
    @FXML private void handleTipoDevolucion() { tipoCita = 1; menuTipoCita.setText("Devolución"); }

    // ── Hora ─────────────────────────────────────────────────────────────────

    @FXML
    private void handleHora(ActionEvent event) {
        horaSeleccionada = ((MenuItem) event.getSource()).getText();
        menuHora.setText(horaSeleccionada);
    }

    // ── Guardar ──────────────────────────────────────────────────────────────

    @FXML
    private void handleGuardar() {
        String fecha  = fieldFecha.getText().trim();
        String motivo = fieldMotivo.getText().trim();

        if (tipoCita == -1) {
            mostrarError("Selecciona un tipo de cita.");
            return;
        }
        if (fecha.isEmpty()) {
            mostrarError("Ingresa la fecha.");
            return;
        }
        if (horaSeleccionada == null) {
            mostrarError("Selecciona una hora.");
            return;
        }
        if (motivo.isEmpty()) {
            mostrarError("Ingresa el motivo.");
            return;
        }
        if (service == null) {
            mostrarError("Sin conexión al servidor.");
            return;
        }

        int clienteId = Sesion.getInstance().getClienteId();
        if (clienteId == -1) {
            mostrarError("Sesión no iniciada.");
            return;
        }

        try {
            String fechaHora = fecha + "T" + horaSeleccionada;
            boolean ok = service.solicitarCita(clienteId, fechaHora, tipoCita, motivo);
            if (ok) {
                labelMensaje.setStyle("-fx-text-fill: green;");
                labelMensaje.setText("Cita solicitada correctamente.");
            } else {
                mostrarError("No se pudo registrar la cita.");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        labelMensaje.setStyle("-fx-text-fill: red;");
        labelMensaje.setText(msg);
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()   { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()  { System.out.println("[SOLICITAR CITA] Ya estás aquí"); }
    @FXML private void handleVerCita()        { System.out.println("[SOLICITAR CITA] Ver cita — pendiente"); }
    @FXML private void handleModificarCita()  { System.out.println("[SOLICITAR CITA] Modificar cita — pendiente"); }
    @FXML private void handleConsultarTurno() { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()      { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCliente.fxml"); }
    @FXML private void handleCancelarCita()   { System.out.println("[SOLICITAR CITA] Cancelar cita — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) menuTipoCita.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
