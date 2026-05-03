package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.Server.Model.interfaces.INotificable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PantallaTurnoController implements INotificable {

    @FXML private Label labelNumeroTurno;
    @FXML private Label labelBanco;
    @FXML private Label labelEspera;

    @Override
    public void notificarTurno(Ticket t) {
        Platform.runLater(() -> {
            labelNumeroTurno.setText("T" + String.format("%03d", t.getNumeroTurno()));
            labelBanco.setText("Banco: " + t.getBancoAsignado().getNombre());
            labelEspera.setText("En espera: " + t.getBancoAsignado().getPersonasEnEspera());
        });
    }

    @Override
    public void notificarCambioEstado(int estado) {
        Platform.runLater(() -> {
            String msg = switch (estado) {
                case 0 -> "En espera: esperando turno";
                case 1 -> "En espera: siendo atendido";
                case 2 -> "En espera: atención finalizada";
                default -> "En espera: -";
            };
            labelEspera.setText(msg);
        });
    }

    /** Llamado desde ServerController para registrar este monitor en un banco. */
    public void reset() {
        Platform.runLater(() -> {
            labelNumeroTurno.setText("T000");
            labelBanco.setText("Banco: -");
            labelEspera.setText("En espera: -");
        });
    }
}