package edu.co.diegoxs96.Server.Model.Monitor;

import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.Server.Model.interfaces.INotificable;

/**
 * Monitor en celular del cliente. Notifica al cliente su turno.
 * Implementa INotificable para recibir eventos del servidor.
 */
public class MonitorCelular implements INotificable {

    private Cliente cliente;
    private Ticket  ticket;

    public MonitorCelular(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    @Override
    public void notificarTurno(Ticket t) {
        System.out.println("[CELULAR] " + cliente.getNombreCompleto()
                + ": es su turno! Ticket T" + t.getNumeroTurno()
                + " - Banco: " + t.getBancoAsignado().getNombre());
    }

    @Override
    public void notificarCambioEstado(int estado) {
        System.out.println("[CELULAR] " + cliente.getNombreCompleto()
                + ": estado de turno actualizado a " + estado);
    }

    public int consultarPosicion(int ticketId) {
        if (ticket == null || ticket.getBancoAsignado() == null) return -1;
        return ticket.getBancoAsignado().obtenerPosicion(ticketId);
    }

    public void mostrarInfoTurno() {
        if (ticket == null) { System.out.println("[CELULAR] Sin ticket activo."); return; }
        System.out.println("[CELULAR] Ticket T" + ticket.getNumeroTurno()
                + " | Banco: " + ticket.getBancoAsignado().getNombre()
                + " | Estado: " + ticket.getEstado());
    }
}
