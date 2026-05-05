package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.TicketDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.Naming;

public class MaquinaEntradaViewController {

    @FXML private TextField fieldIdentificacion;
    @FXML private TextField fieldNumeroCita;
    @FXML private Label     labelResultado;
    @FXML private Label     labelTurno;

    private TicketInterface service;

    @FXML
    private void initialize() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[KIOSCO] Sin conexión al servidor.");
        }
    }

    @FXML
    private void handleSiguiente() {
        String id      = fieldIdentificacion.getText().trim();
        String citaStr = fieldNumeroCita.getText().trim();

        if (id.isEmpty()) {
            mostrarError("Ingresa el número de identificación.");
            return;
        }
        if (citaStr.isEmpty()) {
            mostrarError("Ingresa el número de cita.");
            return;
        }
        if (service == null) {
            mostrarError("Sin conexión al servidor.");
            return;
        }

        try {
            int citaId = Integer.parseInt(citaStr);
            TicketDTO dto = service.procesarEntrada(id, citaId);

            if (dto != null) {
                labelResultado.setStyle("-fx-text-fill: green;");
                labelResultado.setText("Ticket asignado — " + dto.bancoNombre);
                labelTurno.setStyle("-fx-text-fill: #1a1aff;");
                labelTurno.setText("T" + String.format("%03d", dto.numeroTurno));
                limpiarCampos();
            } else {
                mostrarError("No se pudo emitir el ticket. Verifica tus datos.");
                labelTurno.setText("");
            }
        } catch (NumberFormatException e) {
            mostrarError("El número de cita debe ser numérico.");
        } catch (Exception e) {
            mostrarError("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleCerrarSesion() {
        fieldIdentificacion.clear();
        fieldNumeroCita.clear();
        labelResultado.setText("");
        labelTurno.setText("");
    }

    private void mostrarError(String msg) {
        labelResultado.setStyle("-fx-text-fill: red;");
        labelResultado.setText(msg);
    }

    private void limpiarCampos() {
        fieldIdentificacion.clear();
        fieldNumeroCita.clear();
    }
}
