package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Model.interfaces.IUsuario;
import java.io.Serializable;

public abstract class Usuario implements IUsuario, Serializable {

    protected int    id;
    protected String numeroIdentificacion;
    protected String nombres;
    protected String apellidos;
    protected String contrasena;

    public Usuario(int id, String numeroIdentificacion, String nombres, String apellidos, String contrasena) {
        this.id                   = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres              = nombres;
        this.apellidos            = apellidos;
        this.contrasena           = contrasena;
    }

    public int    getId()                   { return id; }
    public String getNumeroIdentificacion() { return numeroIdentificacion; }
    public String getNombres()              { return nombres; }
    public String getApellidos()            { return apellidos; }

    @Override
    public boolean autenticar(String contrasena) { return this.contrasena.equals(contrasena); }

    @Override
    public String getNombreCompleto() { return nombres + " " + apellidos; }
}
