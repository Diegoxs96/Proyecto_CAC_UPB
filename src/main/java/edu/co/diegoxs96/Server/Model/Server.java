package edu.co.diegoxs96.Server.Model;

import edu.co.diegoxs96.Server.Controller.*;
import edu.co.diegoxs96.Server.Model.Service.TicketService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

    private final String ip;
    private final int    port;
    private final String serviceName;
    private final String uri;

    public final GestorClientes           gestorClientes  = new GestorClientes();
    public final GestorCitas              gestorCitas     = new GestorCitas();
    public final GestorBancos             gestorBancos    = new GestorBancos();
    public final GestorTickets            gestorTickets   = new GestorTickets(gestorBancos);
    public final GestorReportes           gestorReportes  = new GestorReportes(gestorCitas);
    public final AdminController          adminController = new AdminController(gestorBancos);
    public final LoginController          loginController = new LoginController(gestorClientes);
    public final PerfilController         perfilController  = new PerfilController(gestorClientes, gestorCitas);
    public final MaquinaEntradaController maquinaEntrada  = new MaquinaEntradaController(gestorClientes, gestorCitas, gestorTickets);

    public Server(String ip, int port, String serviceName) {
        this.ip          = ip;
        this.port        = port;
        this.serviceName = serviceName;
        this.uri         = "//" + ip + ":" + port + "/" + serviceName;
    }

    public boolean deploy() {
        try {
            System.setProperty("java.rmi.server.hostname", ip);
            try { LocateRegistry.createRegistry(port); }
            catch (RemoteException e) { LocateRegistry.getRegistry(port); }

            // Cargar citas persistidas (necesita clientes ya cargados)
            gestorCitas.cargarDesdejSON(gestorClientes.listarTodos());

            TicketService service = new TicketService(gestorClientes, gestorCitas, gestorTickets, gestorBancos);
            Naming.rebind(uri, service);
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean undeploy() {
        try { Naming.unbind(uri); return true; }
        catch (Exception e) { e.printStackTrace(); return false; }
    }
}