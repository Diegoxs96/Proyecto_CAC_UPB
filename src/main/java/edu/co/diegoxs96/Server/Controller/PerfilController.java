package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Cita;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;

/**
 * Muestra y gestiona el perfil de un cliente:
 * datos personales + historial de citas.
 */
public class PerfilController {

    private final GestorClientes gestorClientes;
    private final GestorCitas    gestorCitas;

    public PerfilController(GestorClientes gestorClientes, GestorCitas gestorCitas) {
        this.gestorClientes = gestorClientes;
        this.gestorCitas    = gestorCitas;
    }

    /** Retorna el cliente por número de identificación. */
    public Cliente obtenerPerfil(String numeroIdentificacion) {
        return gestorClientes.buscarPorIdentificacion(numeroIdentificacion);
    }

    /** Citas pendientes del cliente. */
    public LinkedList<Cita> citasActivas(int clienteId) {
        return gestorCitas.listarPorCliente(clienteId);
    }

    /** Citas completadas y no asistidas del cliente (del historial). */
    public LinkedList<Cita> historialCliente(int clienteId) {
        LinkedList<Cita> result = new LinkedList<>();
        edu.co.diegoxs96.structures.model.iterator.Iterator<Cita> it =
                gestorCitas.getHistorico().iterator();
        while (it.hasNext()) {
            Cita c = it.next();
            if (c.getCliente().getId() == clienteId) result.add(c);
        }
        return result;
    }

    /** Resumen en consola — tu compañero puede conectar esto a una vista. */
    public void mostrarPerfil(String numeroIdentificacion) {
        Cliente c = obtenerPerfil(numeroIdentificacion);
        if (c == null) { System.out.println("[PERFIL] Cliente no encontrado."); return; }

        System.out.println("══ PERFIL ══════════════════════════");
        System.out.println("Nombre   : " + c.getNombreCompleto());
        System.out.println("ID       : " + c.getNumeroIdentificacion());
        System.out.println("Edad     : " + c.getEdad());
        System.out.println("Tipo     : " + (c.esPremium() ? "Premium" : "Estándar"));
        System.out.println("Dirección: " + c.getDireccion());

        LinkedList<Cita> activas = citasActivas(c.getId());
        System.out.println("\nCitas pendientes (" + activas.size() + "):");
        edu.co.diegoxs96.structures.model.iterator.Iterator<Cita> it = activas.iterator();
        while (it.hasNext()) {
            Cita cita = it.next();
            System.out.println("  → " + cita.getFechaHora() + " | " + cita.getLugar());
        }

        LinkedList<Cita> hist = historialCliente(c.getId());
        System.out.println("\nHistorial (" + hist.size() + " citas):");
        edu.co.diegoxs96.structures.model.iterator.Iterator<Cita> ih = hist.iterator();
        while (ih.hasNext()) {
            Cita cita = ih.next();
            String estado = cita.getEstado() == Cita.ESTADO_COMPLETADA ? "Completada" : "No asistida";
            System.out.println("  → " + cita.getFechaHora() + " [" + estado + "]");
        }
        System.out.println("════════════════════════════════════");
    }
}
