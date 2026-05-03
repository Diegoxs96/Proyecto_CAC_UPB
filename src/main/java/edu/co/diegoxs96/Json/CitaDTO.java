package edu.co.diegoxs96.Json;

import edu.co.diegoxs96.Server.Model.Cita;

import java.io.Serializable;

public class CitaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public int    id;
    public int    clienteId;
    public String tipo;
    public String fecha;
    public String hora;
    public String banco;
    public String estado;

    public CitaDTO() {}

    public CitaDTO(Cita c) {
        this.id        = c.getId();
        this.clienteId = c.getCliente().getId();
        this.tipo      = tipoLabel(c.getTipoCita());
        this.fecha = extraerFecha(c.getFechaHora());
        this.hora  = extraerHora(c.getFechaHora());
        this.banco = c.getLugar();
        this.estado = estadoLabel(c.getEstado());
    }

    private String tipoLabel(int t) {
        return switch (t) {
            case 0  -> "Reclamo";
            case 1  -> "Devolución";
            case 2  -> "Asesoría";
            default -> "—";
        };
    }

    private String estadoLabel(int e) {
        return switch (e) {
            case 0  -> "Pendiente";
            case 1  -> "Completada";
            case 2  -> "No asistida";
            case 3  -> "Cancelada";
            default -> "—";
        };
    }

    private String extraerFecha(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[0] : dt;
    }

    private String extraerHora(String dt) {
        if (dt == null) return "—";
        return dt.contains("T") ? dt.split("T")[1] : dt;
    }
}