package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrearTicketController {

    @FXML private Label      labelNumeroTicket;
    @FXML private Label      labelFecha;
    @FXML private Label      labelMensaje;
    @FXML private MenuButton menuTipoCita;

    private int tipoCita = -1;

    @FXML
    private void initialize() {
        labelFecha.setText("Fecha: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @FXML private void handleTipoReclamo()    { tipoCita = 0; menuTipoCita.setText("Reclamo"); }
    @FXML private void handleTipoAsesoria()   { tipoCita = 2; menuTipoCita.setText("Asesoría"); }
    @FXML private void handleTipoDevolucion() { tipoCita = 1; menuTipoCita.setText("Devolución"); }

    @FXML
    private void handleCrearTicket() {
        if (tipoCita == -1) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Selecciona un tipo de cita.");
            return;
        }
        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Ticket creado correctamente.");
        labelNumeroTicket.setText("T001");
    }

    @FXML private void handleEditarPerfil()      { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()     { System.out.println("[TICKET] Solicitar cita — pendiente"); }
    @FXML private void handleVerCita()           { System.out.println("[TICKET] Ver cita — pendiente"); }
    @FXML private void handleModificarCita()     { System.out.println("[TICKET] Modificar cita — pendiente"); }
    @FXML private void handleObtenerTicket()     { System.out.println("[TICKET] Ya estás aquí"); }
    @FXML private void handleConsultarTurno()    { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()         { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCliente.fxml"); }
    @FXML private void handleCancelarCita()      { System.out.println("[TICKET] Cancelar cita — pendiente"); }
    @FXML private void handleSolicitarConsulta() { System.out.println("[TICKET] Solicitar consulta — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage = (Stage) menuTipoCita.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}