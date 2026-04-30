package edu.co.diegoxs96.Server.Model;

import java.io.Serializable;

public class Cliente extends Usuario implements Comparable<Cliente>, Serializable {

    public static final int TIPO_ESTANDAR = 0;
    public static final int TIPO_PREMIUM  = 1;

    private int    edad;
    private String direccion;
    private int    tipoCliente;

    public Cliente(int id, String numeroIdentificacion, String nombres,
                   String apellidos, String contrasena, int edad,
                   String direccion, int tipoCliente) {
        super(id, numeroIdentificacion, nombres, apellidos, contrasena);
        this.edad        = edad;
        this.direccion   = direccion;
        this.tipoCliente = tipoCliente;
    }

    public int    getEdad()        { return edad; }
    public String getDireccion()   { return direccion; }
    public int    getTipoCliente() { return tipoCliente; }
    public boolean esMayorDe60()  { return edad > 60; }
    public boolean esPremium()    { return tipoCliente == TIPO_PREMIUM; }

    public int getPrioridad() {
        int p = esPremium() ? 2 : 0;
        if (esMayorDe60()) p++;
        return p;
    }

    /**
     * Verifica si el cliente tiene al menos una cita en estado PENDIENTE.
     * Requiere GestorCitas para consultar sin acoplar el modelo al gestor.
     */
    public boolean tieneCitaVigente(edu.co.diegoxs96.Server.Controller.GestorCitas gestorCitas) {
        return !gestorCitas.listarPorCliente(this.id).isEmpty();
    }

    @Override public int compareTo(Cliente o) { return Integer.compare(this.id, o.id); }
    @Override public boolean equals(Object o) { return o instanceof Cliente && this.id == ((Cliente)o).id; }
    @Override public int hashCode()           { return Integer.hashCode(id); }
    @Override public String toString()        { return id + " - " + getNombreCompleto(); }
}
