package edu.co.diegoxs96.Server.Controller;

import edu.co.diegoxs96.Server.Model.*;
import edu.co.diegoxs96.Server.Model.ColaBancaria;
import edu.co.diegoxs96.Server.Model.ColaPrioridad;
import edu.co.diegoxs96.Server.Model.Monitor.MonitorSala;
import edu.co.diegoxs96.Server.View.ServerView;
import javafx.stage.Stage;

public class ServerController {

    private final Server     model;
    private final ServerView view;

    public ServerController(Server model, ServerView view) {
        this.model = model;
        this.view  = view;
    }

    public void show(Stage stage) {
        view.build(stage);

        view.setOnStart(() -> {
            inicializarBancos();
            boolean ok = model.deploy();
            if (ok) {
                view.log("Servidor CAC-UPB iniciado en " + model.gestorBancos.listarActivos().size() + " bancos.");
                view.log("Gestores activos: Clientes, Citas, Tickets, Bancos, Reportes.");
            } else {
                view.log("Error al iniciar el servidor. Verifique el puerto.");
            }
        });

        view.setOnStop(() -> {
            model.undeploy();
            view.log("Servidor detenido.");
        });
    }

    /** Crea los bancos de servicio por defecto con sus colas. */
    private void inicializarBancos() {
        // Banco 1 — cola con prioridad (premium y mayores de 60 primero)
        BancoServicio b1 = new BancoServicio(1, "Banco Reclamos", "Sala A - Ventanilla 1", 20, 0, new ColaPrioridad());
        // Banco 2 — cola FIFO para devoluciones
        BancoServicio b2 = new BancoServicio(2, "Banco Devoluciones", "Sala A - Ventanilla 2", 20, 1, new ColaBancaria());
        // Banco 3 — cola con prioridad para asesorías
        BancoServicio b3 = new BancoServicio(3, "Banco Asesorías", "Sala B - Ventanilla 1", 15, 2, new ColaPrioridad());

        // Añadir monitor de sala a cada banco
        b1.addMonitor(new MonitorSala(b1));
        b2.addMonitor(new MonitorSala(b2));
        b3.addMonitor(new MonitorSala(b3));

        model.gestorBancos.agregarBanco(b1);
        model.gestorBancos.agregarBanco(b2);
        model.gestorBancos.agregarBanco(b3);

        view.log("Bancos inicializados: Reclamos | Devoluciones | Asesorías");
    }
}