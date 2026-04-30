package edu.co.diegoxs96.Client.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuController {

    @FXML private ImageView imgMenu;

    @FXML
    private void initialize() {
        // Si la imagen no se cargó desde el FXML, poner placeholder
        if (imgMenu != null && imgMenu.getImage() == null) {
            imagenNoDisponible();
        }
    }

    /** Llamado desde FXML si la imagen no se encuentra. */
    public static void imagenNoDisponible() {
        System.out.println("[MENU] Imagen no disponible — coloca menu_imagen.jpg en resources/edu/co/diegoxs96/images/");
    }

    @FXML private void handleEditarPerfil()      { System.out.println("[MENU] Editar perfil"); }
    @FXML private void handleSolicitarCita()     { System.out.println("[MENU] Solicitar cita"); }
    @FXML private void handleVerCita()           { System.out.println("[MENU] Ver cita"); }
    @FXML private void handleModificarCita()     { System.out.println("[MENU] Modificar cita"); }
    @FXML private void handleObtenerTicket()     { System.out.println("[MENU] Obtener ticket"); }
    @FXML private void handleNotificacion()      { System.out.println("[MENU] Notificación de turno"); }
    @FXML private void handleConsultarTurno()    { System.out.println("[MENU] Consultar turno"); }
    @FXML private void handleHistorial()         { System.out.println("[MENU] Historial de citas"); }
    @FXML private void handleCancelarCita()      { System.out.println("[MENU] Cancelar cita"); }
    @FXML private void handleSolicitarConsulta() { System.out.println("[MENU] Solicitar consulta"); }
}
