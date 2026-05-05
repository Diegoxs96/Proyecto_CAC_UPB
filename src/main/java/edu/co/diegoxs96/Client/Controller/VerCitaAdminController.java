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

public class VerCitaAdminController {

    @FXML private TableView<CitaDTO>           tablaCitas;
    @FXML private TableColumn<CitaDTO, String> colNumCita;
    @FXML private TableColumn<CitaDTO, String> colCliente;
    @FXML private TableColumn<CitaDTO, String> colTipo;
    @FXML private TableColumn<CitaDTO, String> colFecha;
    @FXML private TableColumn<CitaDTO, String> colHora;
    @FXML private TableColumn<CitaDTO, String> colBanco;
    @FXML private TableColumn<CitaDTO, String> colEstado;

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
            System.out.println("[VER CITA ADMIN] Sin conexión al servidor.");
        }
    }

    private void configurarColumnas() {
        colNumCita.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().id)));
        colCliente.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().clienteNombre));
        colTipo.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().tipo));
        colFecha.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().fecha));
        colHora.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().hora));
        colBanco.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().banco));
        colEstado.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().estado));
    }

    private void cargarDatos() {
        if (service == null) return;
        try {
            ArrayList<CitaDTO> citas = service.listarTodasCitas();
            tablaCitas.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            System.out.println("[VER CITA ADMIN] Error al cargar: " + e.getMessage());
        }
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()  { navegar("/edu/co/diegoxs96/views/Admin/EditarPerfilAdmin.fxml"); }
    @FXML private void handleBuscarCliente() { navegar("/edu/co/diegoxs96/views/Admin/BuscarCliente.fxml"); }
    @FXML private void handleVerCita()       { System.out.println("[VER CITA ADMIN] Ya estás aquí"); }
    @FXML private void handleModificarCita() { navegar("/edu/co/diegoxs96/views/Admin/ModificarCitaAdmin.fxml"); }
    @FXML private void handleHistorial()     { navegar("/edu/co/diegoxs96/views/Admin/HistorialAdmin.fxml"); }
    @FXML private void handleMenu()          { navegar("/edu/co/diegoxs96/views/Admin/MenuAdmin.fxml"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) tablaCitas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
