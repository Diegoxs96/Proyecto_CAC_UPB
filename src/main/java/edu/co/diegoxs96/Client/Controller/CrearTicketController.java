package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrearTicketController {

    @FXML private Label      labelNumeroTicket;
    @FXML private Label      labelFecha;
    @FXML private Label      labelMensaje;
    @FXML private MenuButton menuTipoCita;

    private int tipoCita = -1;

    @FXML
    private void initialize() {
        labelFecha.setText("Fecha: " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @FXML private void handleTipoReclamo()    { tipoCita = 0; menuTipoCita.setText("Reclamo"); }
    @FXML private void handleTipoAsesoria()   { tipoCita = 2; menuTipoCita.setText("Asesoría"); }
    @FXML private void handleTipoDevolucion() { tipoCita = 1; menuTipoCita.setText("Devolución"); }

    @FXML
    private void handleCrearTicket() {
        if (tipoCita == -1) {
            labelMensaje.setStyle("-fx-text-fill: red;");
            labelMensaje.setText("Selecciona un tipo de cita.");
            return;
        }
        // Aquí se conectará con TicketInterface vía RMI
        labelMensaje.setStyle("-fx-text-fill: green;");
        labelMensaje.setText("Ticket creado correctamente.");
        labelNumeroTicket.setText("T001");
    }
}
