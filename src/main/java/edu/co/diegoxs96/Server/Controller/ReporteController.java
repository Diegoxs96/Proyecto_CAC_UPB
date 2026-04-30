package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Appointment;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Genera reportes de citas no asistidas y completadas.
 *
 * LinkedList<Appointment> → recibe el historial de CitasController
 * y lo filtra linealmente para construir vistas maestro-detalle.
 * La lista enlazada es ideal para este recorrido secuencial.
 */
public class ReporteController {

    /**
     * Filtra del historial solo las citas no asistidas (MISSED).
     * Vista "maestro" del reporte.
     */
    public LinkedList<Appointment> getMissed(LinkedList<Appointment> history) {
        LinkedList<Appointment> result = new LinkedList<>();
        Iterator<Appointment> it = history.iterator();
        while (it.hasNext()) {
            Appointment a = it.next();
            if (a.getStatus() == Appointment.AppointmentStatus.MISSED)
                result.add(a);
        }
        return result;
    }

    /**
     * Filtra del historial solo las citas completadas.
     */
    public LinkedList<Appointment> getCompleted(LinkedList<Appointment> history) {
        LinkedList<Appointment> result = new LinkedList<>();
        Iterator<Appointment> it = history.iterator();
        while (it.hasNext()) {
            Appointment a = it.next();
            if (a.getStatus() == Appointment.AppointmentStatus.COMPLETED)
                result.add(a);
        }
        return result;
    }

    /**
     * Filtra citas de un cliente específico (vista "detalle").
     */
    public LinkedList<Appointment> getByCustomer(LinkedList<Appointment> history, String customerId) {
        LinkedList<Appointment> result = new LinkedList<>();
        Iterator<Appointment> it = history.iterator();
        while (it.hasNext()) {
            Appointment a = it.next();
            if (a.getCustomerId().equals(customerId))
                result.add(a);
        }
        return result;
    }

    public int countMissed(LinkedList<Appointment> history)    { return getMissed(history).size(); }
    public int countCompleted(LinkedList<Appointment> history) { return getCompleted(history).size(); }
}
