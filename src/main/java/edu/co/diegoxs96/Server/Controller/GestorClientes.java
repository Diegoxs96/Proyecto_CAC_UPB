package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.tree.BinaryTree;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

public class GestorClientes {

    private final LinkedList<Cliente> clientes  = new LinkedList<>();
    private final BinaryTree<Cliente> arbol     = new BinaryTree<>();
    private int contadorId = 1;

    public Cliente registrarCliente(String numeroId, String nombres, String apellidos,
                                    String contrasena, int edad, String direccion, int tipo) {
        // Validar duplicado
        if (buscarPorIdentificacion(numeroId) != null) {
            System.out.println("[GESTOR] Ya existe un cliente con identificación: " + numeroId);
            return null;
        }
        Cliente c = new Cliente(contadorId++, numeroId, nombres, apellidos,
                                contrasena, edad, direccion, tipo);
        clientes.add(c);
        arbol.insert(c);
        return c;
    }

    public boolean buscarPorId(int id) {
        return arbol.search(new Cliente(id, "", "", "", "", 0, "", 0));
    }

    public Cliente buscarPorIdentificacion(String num) {
        Iterator<Cliente> it = clientes.iterator();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (c.getNumeroIdentificacion().equals(num)) return c;
        }
        return null;
    }

    public LinkedList<Cliente> listarTodos() { return clientes; }

    public boolean eliminarCliente(int id) {
        Iterator<Cliente> it = clientes.iterator();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (c.getId() == id) {
                clientes.remove(c);
                arbol.remove(c);
                return true;
            }
        }
        return false;
    }

    public int total() { return clientes.size(); }
}
