package edu.co.diegoxs96.Server.Model.Service;

import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.PriorityQueue.PriorityQueueClass;

/**
 * Variante del servicio de tickets con prioridad.
 * Usa PriorityQueueClass respaldada por LinkedList, lo que permite
 * insertar tickets marcados como urgentes (prioridad alta) y atenderlos
 * antes que los normales.
 */
public class PriorityTicketService {

    private final PriorityQueueClass<Ticket> priorityQueue =
            new PriorityQueueClass<>(new LinkedList<>());

    /** Registra un ticket con prioridad explícita (mayor número = mayor prioridad). */
    public boolean register(int priority, Ticket ticket) {
        return priorityQueue.insert(priority, ticket);
    }

    /** Registra un ticket sin prioridad especial. */
    public boolean register(Ticket ticket) {
        return priorityQueue.insert(ticket);
    }

    public Ticket next()    { return priorityQueue.peek(); }
    public Ticket attend()  { return priorityQueue.poll(); }
    public int    pending() { return priorityQueue.size(); }
    public boolean isEmpty(){ return priorityQueue.isEmpty(); }
}
