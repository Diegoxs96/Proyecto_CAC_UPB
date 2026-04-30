package edu.co.diegoxs96.Server.View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ServerView {

    private final String title;
    private TextArea console;
    private Button btnStart;
    private Button btnStop;

    public ServerView(String title) {
        this.title = title;
    }

    /** Construye y muestra la ventana en el Stage recibido. */
    public void build(Stage stage) {
        console  = new TextArea();
        console.setEditable(false);
        console.setWrapText(true);

        btnStart = new Button("Iniciar servidor");
        btnStop  = new Button("Detener servidor");
        btnStop.setDisable(true);

        HBox toolbar = new HBox(10, btnStart, btnStop);
        toolbar.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(console);

        Scene scene = new Scene(root, 600, 400);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /** Agrega una línea al área de consola. */
    public void log(String message) {
        console.appendText(message + "\n");
    }

    /** Asigna la acción del botón Iniciar. */
    public void setOnStart(Runnable action) {
        btnStart.setOnAction(e -> {
            action.run();
            btnStart.setDisable(true);
            btnStop.setDisable(false);
        });
    }

    /** Asigna la acción del botón Detener. */
    public void setOnStop(Runnable action) {
        btnStop.setOnAction(e -> {
            action.run();
            btnStart.setDisable(false);
            btnStop.setDisable(true);
        });
    }
}
