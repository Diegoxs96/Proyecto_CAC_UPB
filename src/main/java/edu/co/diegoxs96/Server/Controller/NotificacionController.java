package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.structures.queue.QueueList;

/**
 * Gestiona las notificaciones pendientes de enviar a los clientes.
 *
 * QueueList<String> → cola FIFO de mensajes pendientes.
 * Las notificaciones se encolan cuando es el turno del cliente
 * y se procesan en orden de llegada.
 */
public class NotificacionController {

    private final QueueList<String> pending = new QueueList<>();

    /**
     * Genera y encola una notificación cuando es el turno del cliente.
     * En una implementación real esto dispararía push/SMS/email.
     */
    public void notifyTurn(Ticket ticket, String bankName) {
        String msg = "Cliente " + ticket.getCustomerId()
                   + ": es su turno en " + bankName
                   + ". Ticket: " + ticket.getId();
        pending.insert(msg);
    }

    /** Notifica cancelación de cita. */
    public void notifyCancellation(String customerId, String appointmentId) {
        pending.insert("Cliente " + customerId
                + ": su cita " + appointmentId + " fue cancelada.");
    }

    /** Procesa (extrae) la siguiente notificación pendiente. */
    public String processNext() {
        return pending.extract();
    }

    public boolean hasPending()   { return !pending.isEmpty(); }
    public int     pendingCount() { return pending.size(); }
}
