package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Json.TicketDTO;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.rmi.Naming;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PantallaTurnoController {

    @FXML private Label labelNumeroTurno;
    @FXML private Label labelBanco;
    @FXML private Label labelEspera;

    private TicketInterface service;
    private int ultimoIdMostrado = -1; // comparar por ID, no por numeroTurno

    @FXML
    private void initialize() {
        labelNumeroTurno.setAlignment(Pos.CENTER);
        labelBanco.setAlignment(Pos.CENTER);
        labelEspera.setAlignment(Pos.CENTER);

        conectar();
        iniciarPolling();
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
            System.out.println("[MONITOR] Conectado al servidor.");
        } catch (Exception e) {
            System.out.println("[MONITOR] Sin conexión al servidor.");
        }
    }

    private void iniciarPolling() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "monitor-polling");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::actualizarMonitor, 0, 2, TimeUnit.SECONDS);
    }

    private void actualizarMonitor() {
        if (service == null) return;
        try {
            TicketDTO enAtencion = service.obtenerTicketEnAtencion();
            if (enAtencion == null) return;

            // Comparar por ID del ticket para detectar cambios correctamente
            if (enAtencion.id == ultimoIdMostrado) return;
            ultimoIdMostrado = enAtencion.id;

            // Contar cuántos siguen esperando en ese banco
            int enEspera = 0;
            try {
                enEspera = (int) service.listarTicketsPorBanco(enAtencion.bancoId)
                        .stream()
                        .filter(t -> t.estado == 0) // ESPERANDO
                        .count();
            } catch (Exception ignored) {}

            final int esperaFinal = enEspera;
            final TicketDTO dto   = enAtencion;
            Platform.runLater(() -> {
                labelNumeroTurno.setText("T" + String.format("%03d", dto.numeroTurno));
                labelBanco.setText(dto.bancoNombre);
                labelEspera.setText("En espera: " + esperaFinal);
            });

            System.out.println("[MONITOR] Turno actualizado: T"
                    + dto.numeroTurno + " — " + dto.bancoNombre);

        } catch (Exception e) {
            System.out.println("[MONITOR] Error al actualizar: " + e.getMessage());
        }
    }
}