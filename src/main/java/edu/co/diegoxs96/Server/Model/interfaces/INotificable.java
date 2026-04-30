package edu.co.diegoxs96.Server.Model.interfaces;

import edu.co.diegoxs96.Server.Model.Ticket;

public interface INotificable {
    void notificarTurno(Ticket t);
    void notificarCambioEstado(int estado);
}
