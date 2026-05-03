package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Model.interfaces.IUsuario;
import java.io.Serializable;

public abstract class Usuario implements IUsuario, Serializable {

    protected int    id;
    protected String numeroIdentificacion;
    protected String nombres;
    protected String apellidos;
    protected String contraseña;

    public Usuario(int id, String numeroIdentificacion, String nombres, String apellidos, String contraseña) {
        this.id                   = id;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres              = nombres;
        this.apellidos            = apellidos;
        this.contraseña           = contraseña;
    }

    public int    getId()  {
        return id;
    }
    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }
    public String getNombres() {
        return nombres;
    }
    public String getApellidos() {
        return apellidos;
    }
    public String getContraseña(){
        return contraseña;
    }

    @Override
    public boolean autenticar(String contrasena) { return this.contraseña.equals(contrasena); }

    @Override
    public String getNombreCompleto() { return nombres + " " + apellidos; }

    public void setContraseña(String v) { this.contraseña = v; }
}