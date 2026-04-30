package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Cita;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;

/**
 * Módulo de Máquina de Entrada (kiosco).
 * Flujo: leer ID → buscar cliente → verificar cita vigente
 *        → calcular prioridad → emitir ticket → asignar banco.
 */
public class MaquinaEntradaController {

    private final GestorClientes gestorClientes;
    private final GestorCitas    gestorCitas;
    private final GestorTickets  gestorTickets;

    public MaquinaEntradaController(GestorClientes gc, GestorCitas gci, GestorTickets gt) {
        this.gestorClientes = gc;
        this.gestorCitas    = gci;
        this.gestorTickets  = gt;
    }

    /**
     * Punto de entrada del kiosco.
     * @param numeroIdentificacion documento del cliente
     * @return Ticket emitido, o null si no puede obtenerlo
     */
    public Ticket procesarEntrada(String numeroIdentificacion) {
        // 1. Buscar cliente
        Cliente cliente = gestorClientes.buscarPorIdentificacion(numeroIdentificacion);
        if (cliente == null) {
            System.out.println("[KIOSCO] Cliente no encontrado: " + numeroIdentificacion);
            return null;
        }

        // 2. Verificar cita vigente — regla de negocio: sin cita no hay ticket
        if (!cliente.tieneCitaVigente(gestorCitas)) {
            System.out.println("[KIOSCO] " + cliente.getNombreCompleto() + " no tiene cita vigente.");
            return null;
        }

        // 3. Tomar la primera cita pendiente del cliente
        LinkedList<Cita> citas = gestorCitas.listarPorCliente(cliente.getId());
        Cita cita = citas.peek();
        if (cita == null) return null;

        // 4. Emitir ticket (prioridad calculada desde el cliente)
        Ticket ticket = gestorTickets.emitirTicket(cita);
        if (ticket == null) {
            System.out.println("[KIOSCO] No hay bancos disponibles en este momento.");
            return null;
        }

        // 5. Confirmación
        System.out.println("[KIOSCO] Ticket emitido: T" + ticket.getNumeroTurno()
                + " | Banco: " + ticket.getBancoAsignado().getNombre()
                + " | Prioridad: " + ticket.getPrioridad()
                + " | Posición: " + ticket.getBancoAsignado().getPersonasEnEspera());
        return ticket;
    }
}
