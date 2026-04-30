package edu.co.diegoxs96.Server.Model;

public class OperadorAtencion extends Usuario {

    private BancoServicio bancoAsignado;
    private boolean       disponible;

    public OperadorAtencion(int id, String numeroIdentificacion, String nombres,
                            String apellidos, String contrasena) {
        super(id, numeroIdentificacion, nombres, apellidos, contrasena);
        this.disponible = true;
    }

    public BancoServicio getBancoAsignado()                { return bancoAsignado; }
    public void          setBancoAsignado(BancoServicio b) { this.bancoAsignado = b; }
    public boolean       isDisponible()                    { return disponible; }

    /**
     * Llama al siguiente ticket del banco asignado.
     * BancoServicio notifica automáticamente a los monitores suscritos.
     */
    public Ticket llamarSiguiente() {
        if (bancoAsignado == null || !disponible) return null;
        Ticket t = bancoAsignado.llamarSiguiente();
        if (t != null) disponible = false;
        return t;
    }

    public void finalizarAtencion(Ticket t) {
        if (t != null) t.completar();
        disponible = true;
    }
}
