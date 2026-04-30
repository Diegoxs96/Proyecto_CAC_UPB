package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.structures.linkedlist.doubly.DoublyLinkedList;

/**
 * Controlador del dashboard.
 *
 * Usa DoublyLinkedList<Ticket> para mantener la lista de tickets visibles.
 * La lista doblemente enlazada permite navegar hacia adelante Y hacia atrás
 * sin recorrer desde el inicio, lo que es clave para una vista de panel
 * donde el operador puede paginar en ambas direcciones.
 */
public class DashboardController {

    private final DoublyLinkedList<Ticket> ticketView = new DoublyLinkedList<>();

    /** Agrega un ticket al final de la vista. */
    public void addTicket(Ticket ticket) {
        ticketView.add(ticket);
    }

    /** Elimina un ticket de la vista (cuando es atendido). */
    public void removeTicket(Ticket ticket) {
        ticketView.remove(ticket);
    }

    /** Primer ticket en la vista. */
    public Ticket first() {
        return ticketView.peek();
    }

    /** Último ticket en la vista (navegación inversa — O(1) en lista doble). */
    public Ticket last() {
        return ticketView.peekLast();
    }

    /** Total de tickets visibles en el dashboard. */
    public int count() {
        return ticketView.size();
    }

    /** Limpia la vista del dashboard. */
    public void clear() {
        ticketView.clear();
    }
}
