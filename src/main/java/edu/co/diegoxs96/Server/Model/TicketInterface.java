package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Json.ClienteDTO;
import edu.co.diegoxs96.Json.TicketDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface TicketInterface extends Remote {

    // Métodos existentes
    Ticket emitirTicket(int clienteId, int citaId) throws RemoteException;
    int    consultarPosicion(int ticketId)          throws RemoteException;
    Ticket verFrente(int bancoId)                   throws RemoteException;

    // Emite un ticket creando la cita internamente (sin citaId previo)
    TicketDTO emitirTicketDirecto(int clienteId, int tipoCita) throws RemoteException;

    // Solicita una cita con fecha, hora y motivo
    boolean solicitarCita(int clienteId, String fechaHora, int tipoCita, String motivo) throws RemoteException;

    // Busca un cliente por su numeroIdentificacion
    ClienteDTO buscarCliente(String numeroIdentificacion) throws RemoteException;

    // Nuevo: trae todos los tickets de un cliente para el historial
    ArrayList<TicketDTO> listarTicketsPorCliente(int clienteId) throws RemoteException;

    // Nuevo: trae todos los tickets del sistema (vista admin)
    ArrayList<TicketDTO> listarTodosTickets() throws RemoteException;
}