package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.TicketDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.Naming;
import java.util.ArrayList;

public class VerCitaBancoController {

    @FXML private MenuButton             menuBanco;
    @FXML private TableView<TicketDTO>   tablaTickets;
    @FXML private TableColumn<TicketDTO, String> colNumCita;
    @FXML private TableColumn<TicketDTO, String> colTipo;
    @FXML private TableColumn<TicketDTO, String> colFecha;
    @FXML private TableColumn<TicketDTO, String> colHora;
    @FXML private TableColumn<TicketDTO, String> colBanco;
    @FXML private TableColumn<TicketDTO, String> colEstado;
    @FXML private Label                  labelInfo;
    @FXML private Label                  labelMensaje;

    private TicketInterface service;
    private int bancoSeleccionado = -1;

    @FXML
    private void initialize() {
        conectar();
        configurarColumnas();
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[BANCO] Sin conexión al servidor.");
        }
    }

    private void configurarColumnas() {
        colNumCita.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().citaId)));
        colTipo.setCellValueFactory(d    -> new SimpleStringProperty(tipoLabel(d.getValue().tipoCita)));
        colFecha.setCellValueFactory(d   -> new SimpleStringProperty(extraerFecha(d.getValue().horaEmision)));
        colHora.setCellValueFactory(d    -> new SimpleStringProperty(extraerHora(d.getValue().horaEmision)));
        colBanco.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().bancoNombre));
        colEstado.setCellValueFactory(d  -> new SimpleStringProperty(estadoLabel(d.getValue().estado)));
    }

    // ── Selección de banco ───────────────────────────────────────────────────

    @FXML private void handleBancoReclamos()     { seleccionarBanco(1, "Banco Reclamos"); }
    @FXML private void handleBancoDevoluciones() { seleccionarBanco(2, "Banco Devoluciones"); }
    @FXML private void handleBancoAsesorias()    { seleccionarBanco(3, "Banco Asesorías"); }

    private void seleccionarBanco(int id, String nombre) {
        bancoSeleccionado = id;
        menuBanco.setText(nombre);
        labelMensaje.setText("");
        cargarTickets();
    }

    private void cargarTickets() {
        if (service == null || bancoSeleccionado == -1) return;
        try {
            ArrayList<TicketDTO> tickets = service.listarTicketsPorBanco(bancoSeleccionado);
            tablaTickets.setItems(FXCollections.observableArrayList(tickets));
            labelInfo.setText("En cola: " + tickets.size());
        } catch (Exception e) {
            System.out.println("[BANCO] Error al cargar: " + e.getMessage());
        }
    }

    // ── Acciones ─────────────────────────────────────────────────────────────

    @FXML
    private void handleCitaAtendida() {
        TicketDTO seleccionado = tablaTickets.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un ticket de la tabla.");
            return;
        }
        if (service == null) { mostrarError("Sin conexión al servidor."); return; }
        try {
            boolean ok = service.marcarAtendido(seleccionado.id);
            if (ok) {
                mostrarOk("Ticket T" + String.format("%03d", seleccionado.numeroTurno) + " marcado como atendido.");
                cargarTickets();
            } else {
                mostrarError("No se pudo marcar el ticket.");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSiguienteCita() {
        if (bancoSeleccionado == -1) {
            mostrarError("Selecciona un banco primero.");
            return;
        }
        if (service == null) { mostrarError("Sin conexión al servidor."); return; }
        try {
            TicketDTO siguiente = service.siguienteCita(bancoSeleccionado);
            if (siguiente != null) {
                mostrarOk("Llamando turno T" + String.format("%03d", siguiente.numeroTurno)
                        + " — " + siguiente.bancoNombre);
                cargarTickets();
            } else {
                mostrarOk("No hay más tickets en la cola.");
            }
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private String tipoLabel(int tipo) {
        return switch (tipo) {
            case 0  -> "Reclamo";
            case 1  -> "Devolución";
            case 2  -> "Asesoría";
            default -> "—";
        };
    }

    private String estadoLabel(int estado) {
        return switch (estado) {
            case 0  -> "Esperando";
            case 1  -> "Atendiendo";
            case 2  -> "Atendido";
            case 3  -> "Cancelado";
            default -> "—";
        };
    }

    private String extraerFecha(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[0] : dt;
    }

    private String extraerHora(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[1] : dt;
    }

    private void mostrarError(String msg) {
        labelMensaje.setStyle("-fx-text-fill: red;");
        labelMensaje.setText(msg);
    }

    private void mostrarOk(String msg) {
        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText(msg);
    }
}
