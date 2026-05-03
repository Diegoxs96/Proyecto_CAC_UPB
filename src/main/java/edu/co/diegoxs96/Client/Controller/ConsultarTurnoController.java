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

public class ConsultarTurnoController {

    @FXML private TextField fieldIdentificacion;
    @FXML private TextField fieldNumeroCita;
    @FXML private Label     labelPosicion;

    private TicketInterface service;

    @FXML
    private void initialize() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[CONSULTAR TURNO] Sin conexión al servidor.");
        }
    }

    @FXML
    private void handleBuscar() {
        String citaStr = fieldNumeroCita.getText().trim();
        if (citaStr.isEmpty()) {
            labelPosicion.setText("Num: ingresa el número de cita.");
            return;
        }
        try {
            int citaId = Integer.parseInt(citaStr);
            if (service != null) {
                int pos = service.consultarPosicion(citaId);
                labelPosicion.setText(pos > 0 ? "Num: " + pos : "Num: sin turno activo");
            } else {
                labelPosicion.setText("Num: sin conexión al servidor.");
            }
        } catch (NumberFormatException e) {
            labelPosicion.setText("Num: el número de cita debe ser numérico.");
        } catch (Exception e) {
            labelPosicion.setText("Num: error — " + e.getMessage());
        }
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()   { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/SolicitarCita.fxml"); }
    @FXML private void handleVerCita()        { navegar("/edu/co/diegoxs96/views/Cliente/VerCita.fxml"); }
    @FXML private void handleModificarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/ModificarCita.fxml"); }
    @FXML private void handleConsultarTurno() { System.out.println("[CONSULTAR TURNO] Ya estás aquí"); }
    @FXML private void handleHistorial()      { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCliente.fxml"); }
    @FXML private void handleCancelarCita()   { System.out.println("[CONSULTAR TURNO] Cancelar cita — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) fieldNumeroCita.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}