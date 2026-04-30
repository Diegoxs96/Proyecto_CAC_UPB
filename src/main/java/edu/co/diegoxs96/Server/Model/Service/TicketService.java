package edu.co.diegoxs96.Server.Model.Service;

import edu.co.diegoxs96.Server.Controller.GestorBancos;
import edu.co.diegoxs96.Server.Controller.GestorCitas;
import edu.co.diegoxs96.Server.Controller.GestorClientes;
import edu.co.diegoxs96.Server.Controller.GestorTickets;
import edu.co.diegoxs96.Server.Model.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TicketService extends UnicastRemoteObject implements TicketInterface {

    private final GestorClientes gestorClientes;
    private final GestorCitas    gestorCitas;
    private final GestorTickets  gestorTickets;
    private final GestorBancos   gestorBancos;

    public TicketService(GestorClientes gc, GestorCitas gci, GestorTickets gt, GestorBancos gb)
            throws RemoteException {
        super();
        this.gestorClientes = gc;
        this.gestorCitas    = gci;
        this.gestorTickets  = gt;
        this.gestorBancos   = gb;
    }

    @Override
    public Ticket emitirTicket(int clienteId, int citaId) throws RemoteException {
        Cita cita = gestorCitas.buscarCita(citaId);
        if (cita == null || !cita.estaVigente()) return null;
        if (cita.getCliente().getId() != clienteId) return null;
        return gestorTickets.emitirTicket(cita);
    }

    @Override
    public int consultarPosicion(int ticketId) throws RemoteException {
        Ticket t = gestorTickets.buscarTicket(ticketId);
        if (t == null || t.getBancoAsignado() == null) return -1;
        return t.getBancoAsignado().obtenerPosicion(ticketId);
    }

    @Override
    public Ticket verFrente(int bancoId) throws RemoteException {
        BancoServicio banco = gestorBancos.buscarPorId(bancoId);
        return (banco != null) ? banco.verFrente() : null;
    }
}
