package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Cita;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.stack.StackList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Genera reportes de citas.
 * StackList<Cita> historico → LIFO para ver la cita más reciente primero.
 */
public class GestorReportes {

    private final GestorCitas      gestorCitas;
    private final StackList<Cita>  historico = new StackList<>();

    public GestorReportes(GestorCitas gestorCitas) {
        this.gestorCitas = gestorCitas;
    }

    /** Carga el historico de citas completadas/no asistidas en la pila. */
    public void cargarHistorico() {
        historico.clear();
        Iterator<Cita> it = gestorCitas.getHistorico().iterator();
        while (it.hasNext()) historico.push(it.next());
    }

    public LinkedList<Cita> reporteNoAsistidas() {
        LinkedList<Cita> result = new LinkedList<>();
        Iterator<Cita> it = gestorCitas.getHistorico().iterator();
        while (it.hasNext()) {
            Cita c = it.next();
            if (c.getEstado() == Cita.ESTADO_NO_ASISTIDA) result.add(c);
        }
        return result;
    }

    public LinkedList<Cita> reporteCompletadas() {
        LinkedList<Cita> result = new LinkedList<>();
        Iterator<Cita> it = gestorCitas.getHistorico().iterator();
        while (it.hasNext()) {
            Cita c = it.next();
            if (c.getEstado() == Cita.ESTADO_COMPLETADA) result.add(c);
        }
        return result;
    }

    public StackList<Cita> verHistorico() { return historico; }
}
