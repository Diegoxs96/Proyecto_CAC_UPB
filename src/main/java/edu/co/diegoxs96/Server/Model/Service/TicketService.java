package edu.co.diegoxs96.Server.Model.Service;

import edu.co.diegoxs96.Json.CitaDTO;
import edu.co.diegoxs96.Json.ClienteDTO;
import edu.co.diegoxs96.Json.TicketDTO;
import edu.co.diegoxs96.Server.Controller.GestorBancos;
import edu.co.diegoxs96.Server.Controller.GestorCitas;
import edu.co.diegoxs96.Server.Controller.GestorClientes;
import edu.co.diegoxs96.Server.Controller.GestorTickets;
import edu.co.diegoxs96.Server.Model.*;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public ArrayList<CitaDTO> listarCitasPorCliente(int clienteId) throws RemoteException {
        ArrayList<CitaDTO> result = new ArrayList<>();
        Iterator<Cita> it = gestorCitas.listarPorCliente(clienteId).iterator();
        while (it.hasNext()) result.add(new CitaDTO(it.next()));
        return result;
    }

    @Override
    public ClienteDTO buscarCliente(String numeroIdentificacion) throws RemoteException {
        Cliente c = gestorClientes.buscarPorIdentificacion(numeroIdentificacion);
        return c != null ? new ClienteDTO(c) : null;
    }

    @Override
    public boolean actualizarPerfil(int clienteId, String correo, String contrasena, String direccion)
            throws RemoteException {
        Cliente cliente = gestorClientes.getClientePorId(clienteId);
        if (cliente == null) return false;
        if (contrasena != null && !contrasena.isEmpty()) cliente.setContraseña(contrasena);
        if (direccion  != null && !direccion.isEmpty())  cliente.setDireccion(direccion);
        gestorClientes.guardarEnJSON();
        return true;
    }

    @Override
    public boolean modificarCita(int clienteId, String fechaHora, int tipoCita, String motivo)
            throws RemoteException {
        // Busca la primera cita vigente del cliente y la modifica
        Iterator<Cita> it = gestorCitas.listarPorCliente(clienteId).iterator();
        while (it.hasNext()) {
            Cita c = it.next();
            if (c.puedeModificarse()) {
                return gestorCitas.modificarCita(c.getId(), fechaHora, "Ventanilla", tipoCita, motivo);
            }
        }
        return false;
    }

    @Override
    public int solicitarCita(int clienteId, String fechaHora, int tipoCita, String motivo)
            throws RemoteException {
        Cliente cliente = gestorClientes.getClientePorId(clienteId);
        if (cliente == null) return -1;
        Cita cita = gestorCitas.solicitarCita(cliente, fechaHora, "Ventanilla", tipoCita, motivo);
        return cita.getId();
    }

    @Override
    public TicketDTO emitirTicketDirecto(int clienteId, int tipoCita) throws RemoteException {
        Cliente cliente = gestorClientes.getClientePorId(clienteId);
        if (cliente == null) {
            System.out.println("[TICKET SERVICE] Cliente no encontrado: " + clienteId);
            return null;
        }
        String fechaHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        Cita cita = gestorCitas.solicitarCita(cliente, fechaHora, "Ventanilla", tipoCita, "Ticket directo");
        Ticket t  = gestorTickets.emitirTicket(cita);
        return t != null ? new TicketDTO(t) : null;
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

    @Override
    public ArrayList<TicketDTO> listarTicketsPorCliente(int clienteId) throws RemoteException {
        ArrayList<TicketDTO> result = new ArrayList<>();
        Iterator<Ticket> it = gestorTickets.listarPorCliente(clienteId).iterator();
        while (it.hasNext()) result.add(new TicketDTO(it.next()));
        return result;
    }

    @Override
    public ArrayList<TicketDTO> listarTodosTickets() throws RemoteException {
        ArrayList<TicketDTO> result = new ArrayList<>();
        Iterator<Ticket> it = gestorTickets.getTicketsActivos().iterator();
        while (it.hasNext()) result.add(new TicketDTO(it.next()));
        return result;
    }
}