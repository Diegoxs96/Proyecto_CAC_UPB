package edu.co.diegoxs96.Json;

import edu.co.diegoxs96.Server.Model.Cliente;

import java.io.Serializable;

// Transforma un objeto Cliente a JSON y permite transferirlo por RMI.
public class ClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public int    id;
    public String numeroIdentificacion;
    public String nombres;
    public String apellidos;
    public String contraseña;
    public int    edad;
    public String direccion;
    public int    tipoCliente;

    public ClienteDTO() {}

    public ClienteDTO(Cliente c) {
        this.id                   = c.getId();
        this.numeroIdentificacion = c.getNumeroIdentificacion();
        this.nombres              = c.getNombres();
        this.apellidos            = c.getApellidos();
        this.contraseña           = c.getContraseña();
        this.edad                 = c.getEdad();
        this.direccion            = c.getDireccion();
        this.tipoCliente          = c.getTipoCliente();
    }

    public Cliente toCliente() {
        return new Cliente(id, numeroIdentificacion, nombres, apellidos,
                contraseña, edad, direccion, tipoCliente);
    }
}