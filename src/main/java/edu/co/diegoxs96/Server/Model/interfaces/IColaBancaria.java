package edu.co.diegoxs96.Server.Model.interfaces;

import edu.co.diegoxs96.Server.Model.Ticket;

public interface IColaBancaria {
    void    encolar(Ticket t);
    Ticket  desencolar();
    Ticket  verFrente();
    boolean estaVacia();
    int     getTamano();
    int     obtenerPosicion(int ticketId);
}
