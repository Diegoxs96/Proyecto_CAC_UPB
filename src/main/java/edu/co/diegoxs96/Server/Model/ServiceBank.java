package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.structures.queue.QueueList;

/**
 * Banco de servicio con su propia cola de tickets independiente.
 * El enunciado exige que cada banco gestione su cola por separado.
 */
public class ServiceBank {

    private String           id;
    private String           name;
    private int              capacity;
    private QueueList<Ticket> queue = new QueueList<>();

    public ServiceBank(String id, String name, int capacity) {
        this.id       = id;
        this.name     = name;
        this.capacity = capacity;
    }

    public String  getId()       { return id; }
    public String  getName()     { return name; }
    public int     getCapacity() { return capacity; }
    public int     queueSize()   { return queue.size(); }
    public boolean isFull()      { return queue.size() >= capacity; }
    public boolean isEmpty()     { return queue.isEmpty(); }

    public boolean enqueue(Ticket ticket) {
        if (isFull()) return false;
        queue.insert(ticket);
        return true;
    }

    public Ticket  attend()  { return queue.extract(); }
    public Ticket  peek()    { return queue.peek(); }

    @Override public String toString() { return name + " (" + queue.size() + "/" + capacity + ")"; }
}
