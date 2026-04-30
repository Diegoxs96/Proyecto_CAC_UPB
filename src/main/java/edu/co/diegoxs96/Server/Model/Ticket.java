package edu.co.diegoxs96.Server.Model;

import java.io.Serializable;

/**
 * estado: 0=ESPERANDO, 1=ATENDIENDO, 2=ATENDIDO, 3=CANCELADO
 */
public class Ticket implements Serializable, Comparable<Ticket> {

    public static final int ESTADO_ESPERANDO  = 0;
    public static final int ESTADO_ATENDIENDO = 1;
    public static final int ESTADO_ATENDIDO   = 2;
    public static final int ESTADO_CANCELADO  = 3;

    private int           id;
    private int           numeroTurno;
    private Cita          cita;
    private int           prioridad;
    private int           estado;
    private String        horaEmision;
    private String        horaVencimiento;
    private BancoServicio bancoAsignado;

    public Ticket(int id, int numeroTurno, Cita cita, int prioridad, BancoServicio banco) {
        this.id              = id;
        this.numeroTurno     = numeroTurno;
        this.cita            = cita;
        this.prioridad       = prioridad;
        this.bancoAsignado   = banco;
        this.estado          = ESTADO_ESPERANDO;
        this.horaEmision     = java.time.LocalTime.now().toString();
    }

    public int           getId()             { return id; }
    public int           getNumeroTurno()    { return numeroTurno; }
    public Cita          getCita()           { return cita; }
    public int           getPrioridad()      { return prioridad; }
    public int           getEstado()         { return estado; }
    public String        getHoraEmision()    { return horaEmision; }
    public String        getHoraVencimiento(){ return horaVencimiento; }
    public BancoServicio getBancoAsignado()  { return bancoAsignado; }

    public void setEstado(int estado)               { this.estado = estado; }
    public void setHoraVencimiento(String hora)     { this.horaVencimiento = hora; }

    public void llamar()    { this.estado = ESTADO_ATENDIENDO; }
    public void completar() { this.estado = ESTADO_ATENDIDO; }

    @Override public int compareTo(Ticket o) { return Integer.compare(o.prioridad, this.prioridad); }
    @Override public boolean equals(Object o){ return o instanceof Ticket && this.id == ((Ticket)o).id; }
    @Override public int hashCode()          { return Integer.hashCode(id); }
    @Override public String toString()       { return "Ticket[T" + numeroTurno + " P=" + prioridad + "]"; }

    public String getCustomerId() {
        return String.valueOf(cita.getCliente().getId());
    }
}
