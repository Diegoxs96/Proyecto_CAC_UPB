package edu.co.diegoxs96.Client.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.TicketInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class LoginViewController {

    @FXML private TextField     fieldIdentificacion;
    @FXML private PasswordField fieldContrasena;
    @FXML private Label         labelError;

    private TicketInterface service;

    // DTO interno para leer el JSON de admins
    private static class AdminDTO {
        String numeroIdentificacion;
        String contrasena;
    }

    @FXML
    private void initialize() {
        conectarServidor();
    }

    @FXML
    private void handleLogin() {
        String id       = fieldIdentificacion.getText().trim();
        String password = fieldContrasena.getText().trim();

        if (id.isEmpty() || password.isEmpty()) {
            labelError.setText("Completa todos los campos.");
            return;
        }

        if (service == null) {
            labelError.setText("Sin conexión al servidor.");
            return;
        }

        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            // Puede ser admin con id no numérico — seguimos igual
        }

        if (esAdmin(id, password)) {
            irAMenuAdmin();
        } else {
            guardarSesion(id);
            irAlMenu();
        }
    }

    //Verifica si el id y contraseña están en data/admins.json
    private boolean esAdmin(String id, String contrasena) {
        File file = new File("data/admins.json");
        if (!file.exists()) return false;

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
            String json = sb.toString();
            return json.contains("\"" + id + "\"")
                    && json.contains("\"" + contrasena + "\"");
        } catch (IOException e) {
            System.out.println("[LOGIN] Error leyendo admins.json: " + e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleCancelar() {
        Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleIrARegistro() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/InicioDeSesion/Registrer.fxml"));
            Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            labelError.setText("Error cargando registro: " + e.getMessage());
        }
    }

    private void irAlMenu() {
        try {
            var url = getClass().getResource("/edu/co/diegoxs96/views/Cliente/Menu.fxml");
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            labelError.setText("Error: " + e.getMessage());
        }
    }

    private void irAMenuAdmin() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/edu/co/diegoxs96/views/Admin/MenuAdmin.fxml"));
            Stage stage = (Stage) fieldIdentificacion.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
            labelError.setText("Error cargando menú admin: " + e.getMessage());
        }
    }

    /**
     * Lee clientes.json con Gson (ya importado) y guarda en Sesion el id
     * del cliente que coincide con el numeroIdentificacion recibido.
     */
    private void guardarSesion(String numeroId) {
        File file = new File("data/clientes.json");
        if (!file.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) sb.append(line);
            Type type = new TypeToken<List<ClienteSimpleDTO>>(){}.getType();
            List<ClienteSimpleDTO> lista = new Gson().fromJson(sb.toString(), type);
            for (ClienteSimpleDTO c : lista) {
                if (numeroId.equals(c.numeroIdentificacion)) {
                    Sesion.getInstance().setCliente(c.id, c.nombres);
                    System.out.println("[LOGIN] Sesión: id=" + c.id + " nombre=" + c.nombres);
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("[LOGIN] No se pudo cargar la sesión: " + e.getMessage());
        }
    }

    // DTO mínimo solo para leer clientes.json
    private static class ClienteSimpleDTO {
        int    id;
        String numeroIdentificacion;
        String nombres;
    }

    private void conectarServidor() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            System.out.println("[LOGIN] Servidor no disponible aún.");
        }
    }
}