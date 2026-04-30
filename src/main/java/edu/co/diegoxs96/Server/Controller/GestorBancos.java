package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.BancoServicio;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

public class GestorBancos {

    private final LinkedList<BancoServicio> bancos = new LinkedList<>();

    public void agregarBanco(BancoServicio b) { bancos.add(b); }

    public BancoServicio buscarPorId(int id) {
        Iterator<BancoServicio> it = bancos.iterator();
        while (it.hasNext()) {
            BancoServicio b = it.next();
            if (b.getId() == id) return b;
        }
        return null;
    }

    public BancoServicio obtenerDisponible(int tipoCita) {
        BancoServicio mejor   = null;
        int           minCarga = Integer.MAX_VALUE;
        Iterator<BancoServicio> it = bancos.iterator();
        while (it.hasNext()) {
            BancoServicio b = it.next();
            if (b.isActivo() && b.estaDisponible() && b.getPersonasEnEspera() < minCarga) {
                mejor    = b;
                minCarga = b.getPersonasEnEspera();
            }
        }
        return mejor;
    }

    public LinkedList<BancoServicio> listarActivos() {
        LinkedList<BancoServicio> activos = new LinkedList<>();
        Iterator<BancoServicio> it = bancos.iterator();
        while (it.hasNext()) {
            BancoServicio b = it.next();
            if (b.isActivo()) activos.add(b);
        }
        return activos;
    }

    public void mostrarEstadoGeneral() {
        Iterator<BancoServicio> it = bancos.iterator();
        while (it.hasNext()) it.next().mostrarCola();
    }
}
