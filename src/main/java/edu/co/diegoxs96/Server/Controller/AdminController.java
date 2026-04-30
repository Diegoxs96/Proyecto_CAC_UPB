package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.BancoServicio;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.structures.tree.BinaryTree;

/**
 * Acciones exclusivas del Administrador.
 * BinaryTree<Cliente> → búsqueda O(log n) para gestión administrativa.
 */
public class AdminController {

    private final BinaryTree<Cliente> arbolClientes = new BinaryTree<>();
    private final GestorBancos        gestorBancos;

    public AdminController(GestorBancos gestorBancos) {
        this.gestorBancos = gestorBancos;
    }

    public boolean addCliente(Cliente c)    { return arbolClientes.insert(c); }
    public boolean findCliente(Cliente c)   { return arbolClientes.search(c); }
    public boolean removeCliente(Cliente c) { return arbolClientes.remove(c); }

    public void configurarBanco(BancoServicio b) { gestorBancos.agregarBanco(b); }

    public void generarReporte() { gestorBancos.mostrarEstadoGeneral(); }
}
