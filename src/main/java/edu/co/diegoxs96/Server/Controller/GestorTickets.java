package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Json.TicketJson;
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
    private final TicketJson         repo           = new TicketJson();
    private int contadorTurno = 1;
    private int contadorId    = 1;
    private Ticket ultimoTicket  = null;
    private Ticket ticketLlamado = null;

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
        ultimoTicket = t;
        guardarEnJSON();
        return t;
    }

    public Ticket getUltimoTicket()          { return ultimoTicket; }
    public void   setUltimoTicket(Ticket t)  { this.ultimoTicket = t; }
    public Ticket getTicketLlamado()          { return ticketLlamado; }
    public void   setTicketLlamado(Ticket t)  { this.ticketLlamado = t; }

    public int calcularPrioridad(Cliente c) { return c.getPrioridad(); }

    public BancoServicio asignarBanco(Ticket t) {
        return gestorBancos.obtenerDisponible(t.getCita().getTipoCita());
    }

    public Ticket buscarTicket(int id) {
        Iterator<Ticket> it = ticketsActivos.iterator();
        while (it.hasNext()) {
            Ticket t = it.next();
            if (t.getId() == id) return t;
        }
        return null;
    }

    public LinkedList<Ticket> getTicketsActivos() { return ticketsActivos; }

    public LinkedList<Ticket> listarPorCliente(int clienteId) {
        LinkedList<Ticket> result = new LinkedList<>();
        Iterator<Ticket> it = ticketsActivos.iterator();
        while (it.hasNext()) {
            Ticket t = it.next();
            if (t.getCita() != null && t.getCita().getCliente().getId() == clienteId)
                result.add(t);
        }
        return result;
    }

    public void guardarEnJSON() { repo.guardar(ticketsActivos); }
}