package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Model.interfaces.IColaBancaria;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.PriorityQueue.PriorityQueueClass;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Cola con prioridad bancaria que implementa IColaBancaria.
 * Usa PriorityQueueClass para atender primero a clientes
 * premium y mayores de 60 años.
 */
public class ColaPrioridad implements IColaBancaria {

    private final PriorityQueueClass<Ticket> queue =
            new PriorityQueueClass<>(new LinkedList<>());

    public void encolar(Ticket t, int prioridad) { queue.insert(prioridad, t); }

    @Override public void    encolar(Ticket t) { queue.insert(t); }
    @Override public Ticket  desencolar()      { return queue.poll(); }
    @Override public Ticket  verFrente()       { return queue.peek(); }
    @Override public boolean estaVacia()       { return queue.isEmpty(); }
    @Override public int     getTamano()       { return queue.size(); }

    @Override
    public int obtenerPosicion(int ticketId) {
        Iterator<Ticket> it = queue.iterator();
        int pos = 1;
        while (it.hasNext()) {
            if (it.next().getId() == ticketId) return pos;
            pos++;
        }
        return -1;
    }
}
