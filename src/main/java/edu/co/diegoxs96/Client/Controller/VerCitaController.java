package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.CitaDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
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

public class VerCitaController {

    @FXML private TableView<CitaDTO>            tablaCitas;
    @FXML private TableColumn<CitaDTO, String>  colNumCita;
    @FXML private TableColumn<CitaDTO, String>  colTipo;
    @FXML private TableColumn<CitaDTO, String>  colFecha;
    @FXML private TableColumn<CitaDTO, String>  colHora;
    @FXML private TableColumn<CitaDTO, String>  colBanco;
    @FXML private TableColumn<CitaDTO, String>  colEstado;

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
            System.out.println("[VER CITA] Sin conexión al servidor.");
        }
    }

    private void configurarColumnas() {
        colNumCita.setCellValueFactory(d ->
                new SimpleStringProperty(String.valueOf(d.getValue().id)));
        colTipo.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().tipo));
        colFecha.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().fecha));
        colHora.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().hora));
        colBanco.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().banco));
        colEstado.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().estado));
    }

    private void cargarDatos() {
        if (service == null) return;
        try {
            int clienteId = Sesion.getInstance().getClienteId();
            ArrayList<CitaDTO> citas = service.listarCitasPorCliente(clienteId);
            tablaCitas.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            System.out.println("[VER CITA] Error al cargar: " + e.getMessage());
        }
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()   { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/SolicitarCita.fxml"); }
    @FXML private void handleVerCita()        { System.out.println("[VER CITA] Ya estás aquí"); }
    @FXML private void handleModificarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/ModificarCita.fxml"); }
    @FXML private void handleConsultarTurno() { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()      { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCliente.fxml"); }
    @FXML private void handleCancelarCita()   { System.out.println("[VER CITA] Cancelar cita — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) tablaCitas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}