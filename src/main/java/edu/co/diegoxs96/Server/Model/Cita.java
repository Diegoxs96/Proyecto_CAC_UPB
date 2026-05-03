package edu.co.diegoxs96.Server.Model;

import java.io.Serializable;

/**
 * tipoCita:  0=RECLAMO, 1=DEVOLUCION, 2=ASESORIA
 * estado:    0=PENDIENTE, 1=COMPLETADA, 2=NO_ASISTIDA, 3=CANCELADA
 */
public class Cita implements Serializable, Comparable<Cita> {

    public static final int TIPO_RECLAMO    = 0;
    public static final int TIPO_DEVOLUCION = 1;
    public static final int TIPO_ASESORIA   = 2;

    public static final int ESTADO_PENDIENTE    = 0;
    public static final int ESTADO_COMPLETADA   = 1;
    public static final int ESTADO_NO_ASISTIDA  = 2;
    public static final int ESTADO_CANCELADA    = 3;

    private int    id;
    private Cliente cliente;
    private String  fechaHora;
    private String  lugar;
    private int     tipoCita;
    private String  descripcionMotivo;
    private int     estado;
    private Ticket  ticket;

    public Cita(int id, Cliente cliente, String fechaHora, String lugar,
                int tipoCita, String descripcionMotivo) {
        this.id               = id;
        this.cliente          = cliente;
        this.fechaHora        = fechaHora;
        this.lugar            = lugar;
        this.tipoCita         = tipoCita;
        this.descripcionMotivo = descripcionMotivo;
        this.estado           = ESTADO_PENDIENTE;
    }

    // Constructor para reconstruir desde JSON (incluye estado guardado)
    public Cita(int id, Cliente cliente, String fechaHora, String lugar,
                int tipoCita, int estado) {
        this.id               = id;
        this.cliente          = cliente;
        this.fechaHora        = fechaHora;
        this.lugar            = lugar;
        this.tipoCita         = tipoCita;
        this.descripcionMotivo = "";
        this.estado           = estado;
    }

    public int     getId()                 { return id; }
    public Cliente getCliente()            { return cliente; }
    public String  getFechaHora()          { return fechaHora; }
    public String  getLugar()              { return lugar; }
    public int     getTipoCita()           { return tipoCita; }
    public String  getDescripcionMotivo()  { return descripcionMotivo; }
    public int     getEstado()             { return estado; }
    public Ticket  getTicket()             { return ticket; }

    public void setFechaHora(String v)         { this.fechaHora = v; }
    public void setLugar(String v)             { this.lugar = v; }
    public void setTipoCita(int v)             { this.tipoCita = v; }
    public void setDescripcionMotivo(String v) { this.descripcionMotivo = v; }
    public void setTicket(Ticket t)            { this.ticket = t; }

    public boolean estaVigente()     { return estado == ESTADO_PENDIENTE; }
    public boolean puedeModificarse(){ return ticket == null && estaVigente(); }

    public void cancelar()       { this.estado = ESTADO_CANCELADA; }
    public void confirmar()      { this.estado = ESTADO_COMPLETADA; }
    public void marcarNoAsistida(){ this.estado = ESTADO_NO_ASISTIDA; }

    @Override public int compareTo(Cita o) { return Integer.compare(this.id, o.id); }
    @Override public boolean equals(Object o){ return o instanceof Cita && this.id == ((Cita)o).id; }
    @Override public int hashCode()          { return Integer.hashCode(id); }
    @Override public String toString()       { return "Cita[" + id + " " + fechaHora + "]"; }
}