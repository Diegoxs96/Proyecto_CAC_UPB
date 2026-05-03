package edu.co.diegoxs96.Client.Controller;

/**
 * Singleton que guarda el estado del usuario logueado.
 * Se llena en LoginViewController y se consulta en los demás controllers.
 */
public class Sesion {

    private static Sesion instancia;

    private int    clienteId     = -1;
    private String clienteNombre = "";

    private Sesion() {}

    public static Sesion getInstance() {
        if (instancia == null) instancia = new Sesion();
        return instancia;
    }

    public int    getClienteId()     { return clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public boolean estaLogueado()    { return clienteId != -1; }

    public void setCliente(int id, String nombre) {
        this.clienteId     = id;
        this.clienteNombre = nombre;
    }

    public void cerrar() {
        clienteId     = -1;
        clienteNombre = "";
    }
}
