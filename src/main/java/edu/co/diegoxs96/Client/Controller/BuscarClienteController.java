package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.rmi.Naming;

public class BuscarClienteController {

    @FXML private TextField  fieldBuscarId;
    @FXML private TextField  fieldNumeroCita;
    @FXML private MenuButton menuPrioridad;
    @FXML private Label      labelResultado;

    private TicketInterface service;
    private int prioridadSeleccionada = 0;

    @FXML
    private void initialize() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[BUSCAR] Sin conexión al servidor.");
        }
    }

    @FXML private void handlePrioridadEstandar() { prioridadSeleccionada = 0; menuPrioridad.setText("Estándar"); }
    @FXML private void handlePrioridadPremium()  { prioridadSeleccionada = 1; menuPrioridad.setText("Premium"); }

    @FXML
    private void handleBuscar() {
        String id       = fieldBuscarId.getText().trim();
        String numCita  = fieldNumeroCita.getText().trim();

        if (id.isEmpty()) {
            labelResultado.setText("Ingresa el ID del cliente.");
            return;
        }

        try {
            int clienteId = Integer.parseInt(id);
            int citaId    = numCita.isEmpty() ? -1 : Integer.parseInt(numCita);

            if (service != null && citaId != -1) {
                int posicion = service.consultarPosicion(citaId);
                labelResultado.setText(posicion > 0
                    ? "Cliente " + clienteId + " — Posición en cola: " + posicion
                    : "No se encontró ticket activo para esa cita.");
            } else {
                labelResultado.setText("Buscando cliente ID: " + clienteId);
            }
        } catch (NumberFormatException e) {
            labelResultado.setText("ID y número de cita deben ser numéricos.");
        } catch (Exception e) {
            labelResultado.setText("Error: " + e.getMessage());
        }
    }

    // ── Botones laterales ──
    @FXML private void handleEditarPerfil()      { System.out.println("[BUSCAR] Editar perfil"); }
    @FXML private void handleSolicitarCita()     { System.out.println("[BUSCAR] Solicitar cita"); }
    @FXML private void handleVerCita()           { System.out.println("[BUSCAR] Ver cita"); }
    @FXML private void handleModificarCita()     { System.out.println("[BUSCAR] Modificar cita"); }
    @FXML private void handleObtenerTicket()     { System.out.println("[BUSCAR] Obtener ticket"); }
    @FXML private void handleConsultarTurno()    { System.out.println("[BUSCAR] Consultar turno"); }
    @FXML private void handleHistorial()         { System.out.println("[BUSCAR] Historial"); }
    @FXML private void handleCancelarCita()      { System.out.println("[BUSCAR] Cancelar cita"); }
    @FXML private void handleSolicitarConsulta() { System.out.println("[BUSCAR] Solicitar consulta"); }
}
