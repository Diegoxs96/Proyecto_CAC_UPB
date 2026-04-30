package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Appointment;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.tree.BinaryTree;

/**
 * Gestiona el ciclo de vida de las citas.
 *
 * BinaryTree<Appointment>  → búsqueda rápida de citas por fecha/hora.
 * LinkedList<Appointment>  → historial de citas completadas y no asistidas
 *                            (append-only, recorrido cronológico).
 */
public class CitasController {

    private final BinaryTree<Appointment>  tree    = new BinaryTree<>();
    private final LinkedList<Appointment>  history = new LinkedList<>();

    /** Registra una nueva cita en el árbol. */
    public boolean register(Appointment appointment) {
        return tree.insert(appointment);
    }

    /**
     * Modifica una cita si aún no ha vencido (estado PENDING).
     * Elimina la vieja del árbol e inserta la actualizada.
     */
    public boolean modify(Appointment old, Appointment updated) {
        if (old.getStatus() != Appointment.AppointmentStatus.PENDING) return false;
        tree.remove(old);
        return tree.insert(updated);
    }

    /**
     * Cancela una cita — según reglas de negocio las canceladas
     * NO se guardan en el historial.
     */
    public boolean cancel(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        return tree.remove(appointment);
    }

    /** Marca una cita como completada y la mueve al historial. */
    public boolean complete(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        tree.remove(appointment);
        history.add(appointment);
        return true;
    }

    /** Marca una cita como no asistida y la mueve al historial. */
    public boolean markMissed(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.MISSED);
        tree.remove(appointment);
        history.add(appointment);
        return true;
    }

    public boolean search(Appointment appointment) { return tree.search(appointment); }

    public LinkedList<Appointment> getHistory()    { return history; }
}
