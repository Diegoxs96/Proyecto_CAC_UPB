package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.rmi.Naming;

public class ConsultarTurnoController {

    @FXML private TextField fieldIdentificacion;
    @FXML private TextField fieldNumeroCita;
    @FXML private Label     labelPosicion;

    private TicketInterface service;

    @FXML
    private void initialize() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[CONSULTAR TURNO] Sin conexión al servidor.");
        }
    }

    @FXML
    private void handleBuscar() {
        String citaStr = fieldNumeroCita.getText().trim();
        if (citaStr.isEmpty()) {
            labelPosicion.setText("Num: ingresa el número de cita.");
            return;
        }
        try {
            int citaId = Integer.parseInt(citaStr);
            if (service != null) {
                int pos = service.consultarPosicion(citaId);
                labelPosicion.setText(pos > 0 ? "Num: " + pos : "Num: sin turno activo");
            } else {
                labelPosicion.setText("Num: sin conexión al servidor.");
            }
        } catch (NumberFormatException e) {
            labelPosicion.setText("Num: el número de cita debe ser numérico.");
        } catch (Exception e) {
            labelPosicion.setText("Num: error — " + e.getMessage());
        }
    }
}
