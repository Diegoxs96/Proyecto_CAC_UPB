package edu.co.diegoxs96.Server.View;

import edu.co.diegoxs96.Server.Model.Monitor.MonitorCelular;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.Server.Model.interfaces.INotificable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Vista JavaFX del monitor celular.
 * Implementa INotificable para recibir eventos del servidor
 * y actualizar la pantalla en tiempo real.
 */
public class MonitorCelularView implements INotificable {

    private final MonitorCelular monitor;

    private Label labelTurno;
    private Label labelBanco;
    private Label labelPosicion;
    private Label labelEstado;

    public MonitorCelularView(MonitorCelular monitor) {
        this.monitor = monitor;
    }

    public void build(Stage stage) {
        labelTurno    = makeLabel("Sin turno activo", 36, FontWeight.BOLD, Color.WHITE);
        labelBanco    = makeLabel("─", 18, FontWeight.NORMAL, Color.LIGHTGRAY);
        labelPosicion = makeLabel("Posición en cola: ─", 16, FontWeight.NORMAL, Color.LIGHTYELLOW);
        labelEstado   = makeLabel("Esperando turno...", 14, FontWeight.NORMAL, Color.LIGHTGREEN);

        Label title = makeLabel("CAC-UPB — Mi Turno", 14, FontWeight.BOLD, Color.ORANGE);

        VBox root = new VBox(14, title, labelTurno, labelBanco, labelPosicion, labelEstado);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a2e;");

        stage.setScene(new Scene(root, 360, 280));
        stage.setTitle("CAC-UPB — Monitor Celular");
        stage.show();
    }

    @Override
    public void notificarTurno(Ticket t) {
        Platform.runLater(() -> {
            labelTurno.setText("T U R N O   T" + t.getNumeroTurno());
            labelBanco.setText("Banco: " + t.getBancoAsignado().getNombre()
                    + "  ·  " + t.getBancoAsignado().getUbicacion());
            labelEstado.setText("¡Es su turno! Diríjase al banco.");
            labelEstado.setTextFill(Color.LIMEGREEN);
        });
        monitor.notificarTurno(t);
    }

    @Override
    public void notificarCambioEstado(int estado) {
        Platform.runLater(() -> {
            String msg = switch (estado) {
                case 0 -> "Esperando turno...";
                case 1 -> "Siendo atendido";
                case 2 -> "Atención finalizada";
                case 3 -> "Turno cancelado";
                default -> "Estado desconocido";
            };
            labelEstado.setText(msg);
        });
        monitor.notificarCambioEstado(estado);
    }

    /** Actualiza la posición en cola — llamar periódicamente. */
    public void actualizarPosicion(int pos) {
        Platform.runLater(() ->
            labelPosicion.setText(pos > 0 ? "Posición en cola: " + pos : "Próximo en ser atendido")
        );
    }

    private Label makeLabel(String text, int size, FontWeight weight, Color color) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", weight, size));
        l.setTextFill(color);
        return l;
    }
}
