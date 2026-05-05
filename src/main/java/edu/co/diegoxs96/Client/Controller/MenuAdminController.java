package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuAdminController {

    @FXML private MenuButton menuDias;
    @FXML private TextField  fieldTiempoAtencion;
    @FXML private TextField  fieldTiempoVencimiento;
    @FXML private TextField  fieldSesionInactiva;
    @FXML private TextField  fieldCapacidadCola;
    @FXML private TextField  fieldMaxSesiones;

    // ── Sidebar ──────────────────────────────────────────────────────────────

    @FXML private void handleEditarPerfil()  { navegar("/edu/co/diegoxs96/views/Admin/EditarPerfilAdmin.fxml"); }
    @FXML private void handleBuscarCliente() { navegar("/edu/co/diegoxs96/views/Admin/BuscarCliente.fxml"); }
    @FXML private void handleVerCita()       { navegar("/edu/co/diegoxs96/views/Admin/VerCitaAdmin.fxml"); }
    @FXML private void handleModificarCita() { navegar("/edu/co/diegoxs96/views/Admin/ModificarCitaAdmin.fxml"); }
    @FXML private void handleHistorial()     { navegar("/edu/co/diegoxs96/views/Admin/HistorialAdmin.fxml"); }
    @FXML private void handleMenu()          { System.out.println("[ADMIN] Ya estás aquí"); }

    @FXML
    private void handleCerrarSesion() {
        Sesion.getInstance().cerrar();
        navegar("/edu/co/diegoxs96/views/InicioDeSesion/login.fxml");
    }

    // ── Días ─────────────────────────────────────────────────────────────────

    @FXML private void handleDiasLV()    { menuDias.setText("Lunes-Viernes"); }
    @FXML private void handleDiasLS()    { menuDias.setText("Lunes-Sábado"); }
    @FXML private void handleDiasTodos() { menuDias.setText("Todos los días"); }

    // ── Guardar configuración ─────────────────────────────────────────────────

    @FXML
    private void handleGuardar() {
        System.out.println("[ADMIN] Configuración guardada:");
        System.out.println("  Días             : " + menuDias.getText());
        System.out.println("  Tiempo atención  : " + fieldTiempoAtencion.getText());
        System.out.println("  Tiempo vencimiento: " + fieldTiempoVencimiento.getText());
        System.out.println("  Sesión inactiva  : " + fieldSesionInactiva.getText());
        System.out.println("  Capacidad cola   : " + fieldCapacidadCola.getText());
        System.out.println("  Max sesiones     : " + fieldMaxSesiones.getText());
    }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage  = (Stage) menuDias.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}