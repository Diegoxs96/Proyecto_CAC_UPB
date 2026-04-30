package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Model.interfaces.IColaBancaria;
import edu.co.diegoxs96.Server.Model.interfaces.INotificable;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

public class BancoServicio {

    private int              id;
    private String           nombre;
    private String           ubicacion;
    private int              capacidadMaxima;
    private IColaBancaria    cola;
    private OperadorAtencion operador;
    private boolean          activo;

    private final LinkedList<INotificable> monitores = new LinkedList<>();

    public BancoServicio(int id, String nombre, String ubicacion,
                         int capacidadMaxima, IColaBancaria cola) {
        this.id              = id;
        this.nombre          = nombre;
        this.ubicacion       = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.cola            = cola;
        this.activo          = true;
    }

    public void addMonitor(INotificable monitor) { monitores.add(monitor); }

    private void notificarMonitores(Ticket t) {
        Iterator<INotificable> it = monitores.iterator();
        while (it.hasNext()) it.next().notificarTurno(t);
    }

    public int    getId()              { return id; }
    public String getNombre()          { return nombre; }
    public String getUbicacion()       { return ubicacion; }
    public int    getCapacidadMaxima() { return capacidadMaxima; }
    public OperadorAtencion getOperador()            { return operador; }
    public void   setOperador(OperadorAtencion o)    { this.operador = o; }
    public boolean isActivo()          { return activo; }
    public void   setActivo(boolean v) { this.activo = v; }

    public void    agregarTicket(Ticket t)  { cola.encolar(t); }
    public Ticket  llamarSiguiente()        {
        Ticket t = cola.desencolar();
        if (t != null) { t.llamar(); notificarMonitores(t); }
        return t;
    }
    public Ticket  verFrente()              { return cola.verFrente(); }
    public int     getPersonasEnEspera()    { return cola.getTamano(); }
    public boolean estaDisponible()         { return activo && cola.getTamano() < capacidadMaxima; }
    public int     obtenerPosicion(int id)  { return cola.obtenerPosicion(id); }

    public void mostrarCola() {
        System.out.println(nombre + " [" + cola.getTamano() + "/" + capacidadMaxima + "] - " + ubicacion);
    }

    @Override public String toString() { return nombre + " (" + cola.getTamano() + "/" + capacidadMaxima + ")"; }
}
