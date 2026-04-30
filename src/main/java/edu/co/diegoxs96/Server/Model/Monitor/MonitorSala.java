package edu.co.diegoxs96.Server.Model.Monitor;

import edu.co.diegoxs96.Server.Model.BancoServicio;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.Server.Model.interfaces.IColaBancaria;
import edu.co.diegoxs96.Server.Model.interfaces.INotificable;

/**
 * Monitor físico en sala. Muestra el estado de la cola en tiempo real.
 * Implementa INotificable para recibir eventos del servidor.
 */
public class MonitorSala implements INotificable {

    private BancoServicio  banco;
    private IColaBancaria  cola;

    public MonitorSala(BancoServicio banco) {
        this.banco = banco;
    }

    @Override
    public void notificarTurno(Ticket t) {
        System.out.println("[MONITOR SALA] Llamando turno: " + t.getNumeroTurno()
                + " - Banco: " + banco.getNombre());
    }

    @Override
    public void notificarCambioEstado(int estado) {
        System.out.println("[MONITOR SALA] Estado actualizado: " + estado);
    }

    public void mostrarCola(IColaBancaria cola) {
        System.out.println("[MONITOR SALA] Personas en espera: " + cola.getTamano());
    }

    public void actualizarPantalla() {
        System.out.println("[MONITOR SALA] " + banco.getNombre()
                + " | Espera: " + banco.getPersonasEnEspera()
                + " | Siguiente: " + (banco.verFrente() != null ? banco.verFrente().getNumeroTurno() : "-"));
    }
}
