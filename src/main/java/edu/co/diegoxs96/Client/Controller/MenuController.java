package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MenuController {

    @FXML private ImageView imgMenu;

    @FXML
    private void initialize() {
        if (imgMenu != null && imgMenu.getImage() == null) {
            try {
                var url = getClass().getResource("/edu/co/diegoxs96/images/menu_imagen.jpg");
                if (url != null) imgMenu.setImage(new Image(url.toString()));
            } catch (Exception e) {
                System.out.println("[MENU] Imagen no cargada.");
            }
        }
    }

    @FXML private void handleEditarPerfil()      { navegar("/edu/co/diegoxs96/views/Cliente/EditarPerfilCliente.fxml"); }
    @FXML private void handleSolicitarCita()     { System.out.println("[MENU] Solicitar cita — pendiente"); }
    @FXML private void handleVerCita()           { System.out.println("[MENU] Ver cita — pendiente"); }
    @FXML private void handleModificarCita()     { System.out.println("[MENU] Modificar cita — pendiente"); }
    @FXML private void handleObtenerTicket()     { navegar("/edu/co/diegoxs96/views/Cliente/CrearTicket.fxml"); }
    @FXML private void handleNotificacion()      { System.out.println("[MENU] Notificación — pendiente"); }
    @FXML private void handleConsultarTurno()    { navegar("/edu/co/diegoxs96/views/Cliente/ConsultarTurno.fxml"); }
    @FXML private void handleHistorial()         { navegar("/edu/co/diegoxs96/views/Cliente/HistorialCita.fxml"); }
    @FXML private void handleCancelarCita()      { System.out.println("[MENU] Cancelar cita — pendiente"); }
    @FXML private void handleSolicitarConsulta() { System.out.println("[MENU] Solicitar consulta — pendiente"); }

    private void navegar(String ruta) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(ruta));
            Stage stage = (Stage) imgMenu.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}