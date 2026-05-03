package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.TicketDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.util.ArrayList;

public class HistorialClienteController {

    @FXML private TableView<TicketDTO>            tablaHistorial;
    @FXML private TableColumn<TicketDTO, Integer> colIdCita;
    @FXML private TableColumn<TicketDTO, String>  colCliente;
    @FXML private TableColumn<TicketDTO, String>  colTipo;
    @FXML private TableColumn<TicketDTO, String>  colFecha;
    @FXML private TableColumn<TicketDTO, String>  colHora;
    @FXML private TableColumn<TicketDTO, String>  colBanco;

    private TicketInterface service;

    @FXML
    private void initialize() {
        conectar();
        configurarColumnas();
        cargarDatos();
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[HISTORIAL CLIENTE] Sin conexión al servidor.");
        }
    }

    private void configurarColumnas() {
        colIdCita.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().citaId).asObject());
        colCliente.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().clienteNombre));
        colTipo.setCellValueFactory(d ->
                new SimpleStringProperty(tipoLabel(d.getValue().tipoCita)));
        colFecha.setCellValueFactory(d ->
                new SimpleStringProperty(extraerFecha(d.getValue().horaEmision)));
        colHora.setCellValueFactory(d ->
                new SimpleStringProperty(extraerHora(d.getValue().horaEmision)));
        colBanco.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().bancoNombre));
    }

    private void cargarDatos() {
        if (service == null) return;
        try {
            int clienteId = Sesion.getInstance().getClienteId();
            ArrayList<TicketDTO> tickets = service.listarTicketsPorCliente(clienteId);
            tablaHistorial.setItems(FXCollections.observableArrayList(tickets));
        } catch (Exception e) {
            System.out.println("[HISTORIAL CLIENTE] Error al cargar datos: " + e.getMessage());
        }
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private String tipoLabel(int tipo) {
        return switch (tipo) {
            case 0  -> "Reclamo";
            case 1  -> "Devolución";
            case 2  -> "Asesoría";
            default -> "—";
        };
    }

    // horaEmision viene como "yyyy-MM-dd'T'HH:mm" desde el servidor
    private String extraerFecha(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[0] : dt;
    }

    private String extraerHora(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[1] : dt;
    }

    // ── Navegación (mismo sidebar que Menu.fxml) ─────────────────────────────

    @FXML private void handleEditarPerfil()      { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()     { System.out.println("[HISTORIAL] Solicitar cita — pendiente"); }
    @FXML private void handleVerCita()           { System.out.println("[HISTORIAL] Ver cita — pendiente"); }
    @FXML private void handleModificarCita()     { System.out.println("[HISTORIAL] Modificar cita — pendiente"); }
    @FXML private void handleObtenerTicket()     { navegar("/edu/co/diegoxs96/views/Cliente/CrearTicket.fxml"); }
    @FXML private void handleNotificacion()      { System.out.println("[HISTORIAL] Notificación — pendiente"); }
    @FXML private void handleConsultarTurno()    { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()         { System.out.println("[HISTORIAL] Ya estás aquí"); }
    @FXML private void handleCancelarCita()      { System.out.println("[HISTORIAL] Cancelar cita — pendiente"); }
    @FXML private void handleSolicitarConsulta() { System.out.println("[HISTORIAL] Solicitar consulta — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) tablaHistorial.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}