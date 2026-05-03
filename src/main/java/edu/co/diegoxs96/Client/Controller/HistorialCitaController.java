package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Server.Model.Cita;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class HistorialCitaController {

    @FXML private TableView<Cita>           tablaCitas;
    @FXML private TableColumn<Cita, String> colIdCita;
    @FXML private TableColumn<Cita, String> colCliente;
    @FXML private TableColumn<Cita, String> colTipo;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colEstado;

    @FXML
    private void initialize() {
        colIdCita.setCellValueFactory(c  -> new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombreCompleto()));
        colTipo.setCellValueFactory(c    -> new SimpleStringProperty(tipoCita(c.getValue().getTipoCita())));
        colFecha.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue().getFechaHora().split(" ")[0]));
        colHora.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue().getFechaHora().contains(" ") ? c.getValue().getFechaHora().split(" ")[1] : "-"));
        colEstado.setCellValueFactory(c  -> new SimpleStringProperty(estadoCita(c.getValue().getEstado())));

        // Aquí se cargarán las citas reales cuando se conecte al servidor
        tablaCitas.setItems(FXCollections.observableArrayList());
    }

    private String tipoCita(int tipo) {
        return switch (tipo) {
            case Cita.TIPO_RECLAMO    -> "Reclamo";
            case Cita.TIPO_DEVOLUCION -> "Devolución";
            case Cita.TIPO_ASESORIA   -> "Asesoría";
            default -> "-";
        };
    }

    private String estadoCita(int estado) {
        return switch (estado) {
            case Cita.ESTADO_PENDIENTE    -> "Pendiente";
            case Cita.ESTADO_COMPLETADA   -> "Completada";
            case Cita.ESTADO_NO_ASISTIDA  -> "No asistida";
            case Cita.ESTADO_CANCELADA    -> "Cancelada";
            default -> "-";
        };
    }

    public void cargarCitas(ObservableList<Cita> citas) {
        tablaCitas.setItems(citas);
    }

    @FXML private void handleEditarPerfil()   { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()  { navegar("/edu/co/diegoxs96/views/Cliente/Menu.fxml"); }
    @FXML private void handleVerCita()        { System.out.println("[HISTORIAL] Ver cita"); }
    @FXML private void handleModificarCita()  { System.out.println("[HISTORIAL] Modificar cita"); }
    @FXML private void handleObtenerTicket()  { navegar("/edu/co/diegoxs96/views/Cliente/CrearTicket.fxml"); }
    @FXML private void handleConsultarTurno() { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleCancelarCita()   { System.out.println("[HISTORIAL] Cancelar cita"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage = (Stage) tablaCitas.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
