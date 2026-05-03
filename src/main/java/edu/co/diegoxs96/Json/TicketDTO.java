package edu.co.diegoxs96.Json;

import edu.co.diegoxs96.Server.Model.Ticket;

import java.io.Serializable;

// Transforma un Ticket a JSON y permite transferirlo por RMI.
public class TicketDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public int    id;
    public int    numeroTurno;
    public int    citaId;
    public int    clienteId;
    public String clienteNombre;
    public int    tipoCita;
    public int    prioridad;
    public int    estado;
    public String horaEmision;
    public String horaVencimiento;
    public int    bancoId;
    public String bancoNombre;

    public TicketDTO() {}

    public TicketDTO(Ticket t) {
        this.id              = t.getId();
        this.numeroTurno     = t.getNumeroTurno();
        this.citaId          = t.getCita() != null ? t.getCita().getId() : -1;
        this.clienteId       = t.getCita() != null ? t.getCita().getCliente().getId() : -1;
        this.clienteNombre   = t.getCita() != null ? t.getCita().getCliente().getNombreCompleto() : "";
        this.tipoCita        = t.getCita() != null ? t.getCita().getTipoCita() : -1;
        this.prioridad       = t.getPrioridad();
        this.estado          = t.getEstado();
        this.horaEmision     = t.getHoraEmision();
        this.horaVencimiento = t.getHoraVencimiento();
        this.bancoId         = t.getBancoAsignado() != null ? t.getBancoAsignado().getId() : -1;
        this.bancoNombre     = t.getBancoAsignado() != null ? t.getBancoAsignado().getNombre() : "";
    }
}