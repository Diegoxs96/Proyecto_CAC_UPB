package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.*;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Emite tickets y los asigna al banco correcto.
 * LinkedList<Ticket> ticketsActivos → seguimiento de todos los tickets en curso.
 */
public class GestorTickets {

    private final LinkedList<Ticket> ticketsActivos = new LinkedList<>();
    private final GestorBancos       gestorBancos;
    private int contadorTurno = 1;
    private int contadorId    = 1;

    public GestorTickets(GestorBancos gestorBancos) {
        this.gestorBancos = gestorBancos;
    }

    public Ticket emitirTicket(Cita cita) {
        BancoServicio banco = gestorBancos.obtenerDisponible(cita.getTipoCita());
        if (banco == null) return null;

        int prioridad = calcularPrioridad(cita.getCliente());
        Ticket t = new Ticket(contadorId++, contadorTurno++, cita, prioridad, banco);

        banco.agregarTicket(t);
        ticketsActivos.add(t);
        cita.setTicket(t);
        return t;
    }

    public int calcularPrioridad(Cliente c) { return c.getPrioridad(); }

    public BancoServicio asignarBanco(Ticket t) { return gestorBancos.obtenerDisponible(t.getCita().getTipoCita()); }

    public Ticket buscarTicket(int id) {
        Iterator<Ticket> it = ticketsActivos.iterator();
        while (it.hasNext()) {
            Ticket t = it.next();
            if (t.getId() == id) return t;
        }
        return null;
    }

    public LinkedList<Ticket> getTicketsActivos() { return ticketsActivos; }
}
