package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.CitaDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.rmi.Naming;
import java.util.ArrayList;

public class ModificarCitaAdminController {

    // ── Tabla ────────────────────────────────────────────────────────────────
    @FXML private TableView<CitaDTO>           tablaCitas;
    @FXML private TableColumn<CitaDTO, String> colId;
    @FXML private TableColumn<CitaDTO, String> colCliente;
    @FXML private TableColumn<CitaDTO, String> colTipoT;
    @FXML private TableColumn<CitaDTO, String> colFechaT;
    @FXML private TableColumn<CitaDTO, String> colHoraT;
    @FXML private TableColumn<CitaDTO, String> colEstadoT;

    // ── Formulario ───────────────────────────────────────────────────────────
    @FXML private MenuButton menuTipoCita;
    @FXML private MenuButton menuHora;
    @FXML private TextField  fieldFecha;
    @FXML private TextField  fieldMotivo;
    @FXML private Label      labelMensaje;

    private int    tipoCita = -1;
    private String horaSeleccionada = null;
    private int    citaSeleccionadaId = -1;
    private TicketInterface service;

    @FXML
    private void initialize() {
        conectar();
        configurarTabla();
        cargarCitas();
        tablaCitas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, cita) -> { if (cita != null) precargarFormulario(cita); });
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[MODIFICAR CITA ADMIN] Sin conexión al servidor.");
        }
    }

    private void configurarTabla() {
        colId.setCellValueFactory(d       -> new SimpleStringProperty(String.valueOf(d.getValue().id)));
        colCliente.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().clienteNombre));
        colTipoT.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().tipo));
        colFechaT.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().fecha));
        colHoraT.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().hora));
        colEstadoT.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().estado));
    }

    private void cargarCitas() {
        if (service == null) return;
        try {
            ArrayList<CitaDTO> citas = service.listarTodasCitas();
            tablaCitas.setItems(FXCollections.observableArrayList(citas));
        } catch (Exception e) {
            System.out.println("[MODIFICAR CITA ADMIN] Error al cargar: " + e.getMessage());
        }
    }

    private void precargarFormulario(CitaDTO cita) {
        citaSeleccionadaId = cita.id;
        fieldFecha.setText(cita.fecha);
        fieldMotivo.setText("");
        menuTipoCita.setText(cita.tipo);
        menuHora.setText(cita.hora);
        tipoCita = switch (cita.tipo) {
            case "Reclamo"    -> 0;
            case "Devolución" -> 1;
            case "Asesoría"   -> 2;
            default           -> -1;
        };
        horaSeleccionada = cita.hora;
        labelMensaje.setText("");
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
        if (citaSeleccionadaId == -1)         { mostrarError("Selecciona una cita de la tabla."); return; }
        if (tipoCita == -1)                    { mostrarError("Selecciona un tipo de cita."); return; }
        if (fieldFecha.getText().trim().isEmpty()) { mostrarError("Ingresa la fecha."); return; }
        if (horaSeleccionada == null)          { mostrarError("Selecciona una hora."); return; }
        if (service == null)                   { mostrarError("Sin conexión al servidor."); return; }

        try {
            String fechaHora = fieldFecha.getText().trim() + "T" + horaSeleccionada;
            String motivo    = fieldMotivo.getText().trim();
            boolean ok = service.modificarCitaAdmin(citaSeleccionadaId, fechaHora, tipoCita, motivo);
            if (ok) {
                labelMensaje.setStyle("-fx-text-fill: green;");
                labelMensaje.setText("Cita #" + citaSeleccionadaId + " modificada correctamente.");
                cargarCitas();
            } else {
                mostrarError("La cita no puede modificarse (ya tiene ticket o está cancelada).");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelarCita() {
        System.out.println("[MODIFICAR CITA ADMIN] Cancelar cita — pendiente");
    }

    private void mostrarError(String msg) {
        labelMensaje.setStyle("-fx-text-fill: red;");
        labelMensaje.setText(msg);
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()  { navegar("/edu/co/diegoxs96/views/Admin/EditarPerfilAdmin.fxml"); }
    @FXML private void handleBuscarCliente() { navegar("/edu/co/diegoxs96/views/Admin/BuscarCliente.fxml"); }
    @FXML private void handleVerCita()       { navegar("/edu/co/diegoxs96/views/Admin/VerCitaAdmin.fxml"); }
    @FXML private void handleModificarCita() { System.out.println("[MODIFICAR CITA ADMIN] Ya estás aquí"); }
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
