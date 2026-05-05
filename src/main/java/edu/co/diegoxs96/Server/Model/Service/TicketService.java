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
    public ArrayList<CitaDTO> listarTodasCitas() throws RemoteException {
        ArrayList<CitaDTO> result = new ArrayList<>();
        Iterator<Cita> it = gestorCitas.getCitas().iterator();
        while (it.hasNext()) result.add(new CitaDTO(it.next()));
        return result;
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
    public boolean modificarCitaAdmin(int citaId, String fechaHora, int tipoCita, String motivo)
            throws RemoteException {
        return gestorCitas.modificarCita(citaId, fechaHora, "Ventanilla", tipoCita, motivo);
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
    public TicketDTO procesarEntrada(String numeroIdentificacion, int citaId) throws RemoteException {
        Cliente cliente = gestorClientes.buscarPorIdentificacion(numeroIdentificacion);
        if (cliente == null) {
            System.out.println("[KIOSCO] Cliente no encontrado: " + numeroIdentificacion);
            return null;
        }
        Cita cita = gestorCitas.buscarCita(citaId);
        if (cita == null || !cita.estaVigente() || cita.getCliente().getId() != cliente.getId()) {
            System.out.println("[KIOSCO] Cita inválida o no pertenece al cliente.");
            return null;
        }
        Ticket t = gestorTickets.emitirTicket(cita);
        if (t == null) {
            System.out.println("[KIOSCO] No hay bancos disponibles.");
            return null;
        }
        System.out.println("[KIOSCO] Ticket T" + t.getNumeroTurno() + " emitido para " + cliente.getNombreCompleto());
        return new TicketDTO(t);
    }

    @Override
    public ArrayList<TicketDTO> listarTicketsPorBanco(int bancoId) throws RemoteException {
        ArrayList<TicketDTO> result = new ArrayList<>();
        Iterator<Ticket> it = gestorTickets.getTicketsActivos().iterator();
        while (it.hasNext()) {
            Ticket t = it.next();
            if (t.getBancoAsignado() != null
                    && t.getBancoAsignado().getId() == bancoId
                    && t.getEstado() != Ticket.ESTADO_ATENDIDO
                    && t.getEstado() != Ticket.ESTADO_CANCELADO)
                result.add(new TicketDTO(t));
        }
        // Atendiendo primero, luego por prioridad descendente
        result.sort((a, b) -> {
            boolean aAtendiendo = a.estado == 1;
            boolean bAtendiendo = b.estado == 1;
            if (aAtendiendo != bAtendiendo) return aAtendiendo ? -1 : 1;
            return Integer.compare(b.prioridad, a.prioridad);
        });
        return result;
    }

    @Override
    public boolean marcarAtendido(int ticketId) throws RemoteException {
        Ticket t = gestorTickets.buscarTicket(ticketId);
        if (t == null) return false;
        t.completar();
        gestorTickets.guardarEnJSON();
        return true;
    }

    @Override
    public TicketDTO obtenerTicketEnAtencion() throws RemoteException {
        Ticket t = gestorTickets.getTicketLlamado();
        return t != null ? new TicketDTO(t) : null;
    }

    @Override
    public TicketDTO siguienteCita(int bancoId) throws RemoteException {
        BancoServicio banco = gestorBancos.buscarPorId(bancoId);
        if (banco == null) return null;
        Ticket siguiente = banco.llamarSiguiente();
        if (siguiente == null) return null;
        gestorTickets.guardarEnJSON();
        // Solo actualiza ticketLlamado — el monitor usa este, no ultimoTicket
        gestorTickets.setTicketLlamado(siguiente);
        return new TicketDTO(siguiente);
    }

    @Override
    public TicketDTO obtenerUltimoTicket() throws RemoteException {
        Ticket t = gestorTickets.getUltimoTicket();
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