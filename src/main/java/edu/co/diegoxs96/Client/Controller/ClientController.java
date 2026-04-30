package edu.co.diegoxs96.Client.Controller;

import edu.co.diegoxs96.Client.View.ClientView;
import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.Server.Model.TicketInterface;

import java.rmi.Naming;

public class ClientController {

    private final ClientView     view;
    private       TicketInterface service;

    public ClientController(ClientView view) {
        this.view = view;
    }

    public void init() {
        conectar();

        view.setOnRegister(() -> {
            try {
                String idStr  = view.getFieldId().trim();
                String citaStr = view.getFieldName().trim();
                if (idStr.isEmpty() || citaStr.isEmpty()) {
                    view.setStatus("Completa ID de cliente y número de cita.");
                    return;
                }
                int clienteId = Integer.parseInt(idStr);
                int citaId    = Integer.parseInt(citaStr);

                Ticket ticket = service.emitirTicket(clienteId, citaId);
                if (ticket == null) {
                    view.setStatus("No se pudo emitir ticket. Verifica que tengas una cita vigente.");
                } else {
                    int pos = service.consultarPosicion(ticket.getId());
                    view.setStatus("Ticket T" + ticket.getNumeroTurno()
                            + " emitido | Banco: " + ticket.getBancoAsignado().getNombre()
                            + " | Posición en cola: " + pos);
                }
            } catch (NumberFormatException e) {
                view.setStatus("ID de cliente y número de cita deben ser numéricos.");
            } catch (Exception e) {
                view.setStatus("Error de conexión: " + e.getMessage());
            }
        });
    }

    private void conectar() {
        try {
            Environment env = Environment.getInstance();
            String uri = "//" + env.getIp() + ":" + env.getPort() + "/" + env.getServiceName();
            service = (TicketInterface) Naming.lookup(uri);
        } catch (Exception e) {
            view.setStatus("Sin conexión al servidor. Reintentando al registrar.");
        }
    }
}
