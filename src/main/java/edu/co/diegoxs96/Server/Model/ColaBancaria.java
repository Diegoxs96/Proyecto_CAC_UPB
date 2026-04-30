package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Model.interfaces.IColaBancaria;
import edu.co.diegoxs96.structures.queue.QueueList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

/**
 * Cola FIFO bancaria que implementa IColaBancaria usando QueueList.
 * Adapta los nombres del diagrama (encolar/desencolar/verFrente)
 * a los métodos de la estructura (insert/extract/peek).
 */
public class ColaBancaria implements IColaBancaria {

    private final QueueList<Ticket> queue = new QueueList<>();

    @Override public void   encolar(Ticket t)  { queue.insert(t); }
    @Override public Ticket desencolar()        { return queue.extract(); }
    @Override public Ticket verFrente()         { return queue.peek(); }
    @Override public boolean estaVacia()        { return queue.isEmpty(); }
    @Override public int    getTamano()         { return queue.size(); }

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
