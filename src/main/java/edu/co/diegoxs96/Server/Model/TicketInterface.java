package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Json.CitaDTO;
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

    // Actualiza correo/contraseña/dirección del cliente
    boolean actualizarPerfil(int clienteId, String correo, String contrasena, String direccion) throws RemoteException;

    // Solicita una cita con fecha, hora y motivo — retorna el id asignado, -1 si falla
    int solicitarCita(int clienteId, String fechaHora, int tipoCita, String motivo) throws RemoteException;

    // Modifica la primera cita vigente del cliente
    boolean modificarCita(int clienteId, String fechaHora, int tipoCita, String motivo) throws RemoteException;

    // Busca un cliente por su numeroIdentificacion
    ClienteDTO buscarCliente(String numeroIdentificacion) throws RemoteException;

    // Trae las citas activas de un cliente para VerCita
    ArrayList<CitaDTO> listarCitasPorCliente(int clienteId) throws RemoteException;

    // Nuevo: trae todos los tickets de un cliente para el historial
    ArrayList<TicketDTO> listarTicketsPorCliente(int clienteId) throws RemoteException;

    // Nuevo: trae todos los tickets del sistema (vista admin)
    ArrayList<TicketDTO> listarTodosTickets() throws RemoteException;
}