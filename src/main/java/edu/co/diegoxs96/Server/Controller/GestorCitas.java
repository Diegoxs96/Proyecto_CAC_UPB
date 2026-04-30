package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.Cita;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.Server.Model.History.History;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

public class GestorCitas {

    private final LinkedList<Cita> citas    = new LinkedList<>();
    private final LinkedList<Cita> historico = new LinkedList<>();
    private int contadorId = 1;

    public Cita solicitarCita(Cliente cliente, String fechaHora, String lugar,
                               int tipoCita, String descripcion) {
        Cita c = new Cita(contadorId++, cliente, fechaHora, lugar, tipoCita, descripcion);
        citas.add(c);
        History.getInstance().record("CITA", "Cita " + c.getId() + " registrada para " + cliente.getNombreCompleto());
        return c;
    }

    public boolean modificarCita(int id, String fechaHora, String lugar,
                                  int tipoCita, String descripcion) {
        Cita c = buscarCita(id);
        if (c == null || !c.puedeModificarse()) return false;
        c.setFechaHora(fechaHora);
        c.setLugar(lugar);
        c.setTipoCita(tipoCita);
        c.setDescripcionMotivo(descripcion);
        History.getInstance().record("CITA", "Cita " + id + " modificada.");
        return true;
    }

    public boolean cancelarCita(int id) {
        Cita c = buscarCita(id);
        if (c == null || !c.estaVigente()) return false;
        c.cancelar();
        citas.remove(c);
        History.getInstance().record("CITA", "Cita " + id + " cancelada.");
        return true;
    }

    public Cita buscarCita(int id) {
        Iterator<Cita> it = citas.iterator();
        while (it.hasNext()) { Cita c = it.next(); if (c.getId() == id) return c; }
        return null;
    }

    public LinkedList<Cita> listarPorCliente(int clienteId) {
        LinkedList<Cita> result = new LinkedList<>();
        Iterator<Cita> it = citas.iterator();
        while (it.hasNext()) { Cita c = it.next(); if (c.getCliente().getId() == clienteId) result.add(c); }
        return result;
    }

    public void completar(Cita c)  { c.confirmar();        citas.remove(c); historico.add(c); History.getInstance().record("CITA", "Cita " + c.getId() + " completada."); }
    public void noAsistida(Cita c) { c.marcarNoAsistida(); citas.remove(c); historico.add(c); History.getInstance().record("CITA", "Cita " + c.getId() + " no asistida."); }

    public LinkedList<Cita> listarNoAsistidas() {
        LinkedList<Cita> r = new LinkedList<>();
        Iterator<Cita> it = historico.iterator();
        while (it.hasNext()) { Cita c = it.next(); if (c.getEstado() == Cita.ESTADO_NO_ASISTIDA) r.add(c); }
        return r;
    }

    public LinkedList<Cita> getHistorico() { return historico; }
    public LinkedList<Cita> getCitas()     { return citas; }
}
