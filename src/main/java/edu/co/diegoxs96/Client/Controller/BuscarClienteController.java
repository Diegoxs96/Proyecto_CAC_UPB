package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.ClienteDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        String id      = fieldBuscarId.getText().trim();
        String numCita = fieldNumeroCita.getText().trim();

        if (id.isEmpty()) {
            labelResultado.setStyle("-fx-text-fill: red;");
            labelResultado.setText("Ingresa el ID del cliente.");
            return;
        }
        if (service == null) {
            labelResultado.setStyle("-fx-text-fill: red;");
            labelResultado.setText("Sin conexión al servidor.");
            return;
        }
        try {
            // Buscar cliente por numeroIdentificacion
            ClienteDTO cliente = service.buscarCliente(id);
            if (cliente == null) {
                labelResultado.setStyle("-fx-text-fill: red;");
                labelResultado.setText("Cliente no encontrado.");
                return;
            }

            String tipo = cliente.tipoCliente == 1 ? "Premium" : "Estándar";
            String info = cliente.nombres + " " + cliente.apellidos + " | " + tipo + " | Edad: " + cliente.edad;

            // Si también ingresaron número de cita, mostrar posición en cola
            if (!numCita.isEmpty()) {
                int citaId   = Integer.parseInt(numCita);
                int posicion = service.consultarPosicion(citaId);
                info += posicion > 0
                        ? " | Posición en cola: " + posicion
                        : " | Sin ticket activo para esa cita";
            }

            labelResultado.setStyle("-fx-text-fill: black;");
            labelResultado.setText(info);

        } catch (NumberFormatException e) {
            labelResultado.setStyle("-fx-text-fill: red;");
            labelResultado.setText("Número de cita debe ser numérico.");
        } catch (Exception e) {
            labelResultado.setStyle("-fx-text-fill: red;");
            labelResultado.setText("Error: " + e.getMessage());
        }
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()  { navegar("/edu/co/diegoxs96/views/Admin/EditarPerfilAdmin.fxml"); }
    @FXML private void handleBuscarCliente() { System.out.println("[BUSCAR] Ya estás aquí"); }
    @FXML private void handleVerCita()       { System.out.println("[BUSCAR] Ver cita — pendiente"); }
    @FXML private void handleModificarCita() { System.out.println("[BUSCAR] Modificar cita — pendiente"); }
    @FXML private void handleHistorial()     { navegar("/edu/co/diegoxs96/views/Admin/HistorialAdmin.fxml"); }
    @FXML private void handleMenu()          { navegar("/edu/co/diegoxs96/views/Admin/MenuAdmin.fxml"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) fieldBuscarId.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}