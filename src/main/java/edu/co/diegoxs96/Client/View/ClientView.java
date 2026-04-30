package edu.co.diegoxs96.Client.View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientView {

    private TextField fieldId;
    private TextField fieldName;   // reused for citaId
    private Button    btnRegister;
    private Label     labelStatus;

    public void build(Stage stage) {
        fieldId   = new TextField();
        fieldId.setPromptText("ID del cliente (número)");

        fieldName = new TextField();
        fieldName.setPromptText("Número de cita");

        btnRegister  = new Button("Obtener Ticket");
        labelStatus  = new Label("Ingresa tus datos para obtener un ticket.");

        VBox root = new VBox(10, new Label("CAC-UPB — Máquina de Tickets"),
                             fieldId, fieldName, btnRegister, labelStatus);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 420, 280));
        stage.setTitle("CAC-UPB — Cliente");
        stage.show();
    }

    public String  getFieldId()   { return fieldId.getText(); }
    public String  getFieldName() { return fieldName.getText(); }
    public void    setStatus(String msg) { labelStatus.setText(msg); }
    public void    setOnRegister(Runnable r) { btnRegister.setOnAction(e -> r.run()); }
}
