package edu.co.diegoxs96.Server.Model.interfaces;

public interface IUsuario {
    boolean autenticar(String contrasena);
    String  getNombreCompleto();
}
