package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Administrador;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.Server.Model.OperadorAtencion;
import edu.co.diegoxs96.Server.Model.Usuario;

/**
 * Autentica los tres tipos de usuario del sistema.
 */
public class LoginController {

    private final GestorClientes gestorClientes;

    public LoginController(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
    }

    public Usuario login(String numeroIdentificacion, String contrasena) {
        Cliente c = gestorClientes.buscarPorIdentificacion(numeroIdentificacion);
        if (c != null && c.autenticar(contrasena)) return c;
        return null;
    }

    public boolean autenticarAdmin(Administrador admin, String contrasena) {
        return admin.autenticar(contrasena);
    }

    public boolean autenticarOperador(OperadorAtencion op, String contrasena) {
        return op.autenticar(contrasena);
    }
}
