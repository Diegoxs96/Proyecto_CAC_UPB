package edu.co.diegoxs96.Server.Model;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicketInterface extends Remote {
    Ticket emitirTicket(int clienteId, int citaId) throws RemoteException;
    int    consultarPosicion(int ticketId)          throws RemoteException;
    Ticket verFrente(int bancoId)                   throws RemoteException;
}
