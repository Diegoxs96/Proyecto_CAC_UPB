package edu.co.diegoxs96.Server.Model;

public class Administrador extends Usuario {

    private int nivelAcceso;

    public Administrador(int id, String numeroIdentificacion, String nombres,
                         String apellidos, String contrasena, int nivelAcceso) {
        super(id, numeroIdentificacion, nombres, apellidos, contrasena);
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAcceso() { return nivelAcceso; }
}
