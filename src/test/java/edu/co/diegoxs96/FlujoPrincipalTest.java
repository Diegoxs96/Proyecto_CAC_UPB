package edu.co.diegoxs96;

import edu.co.diegoxs96.Server.Controller.*;
import edu.co.diegoxs96.Server.Model.*;
import edu.co.diegoxs96.Server.Model.ColaPrioridad;

/**
 * Test de flujo de punta a punta sin JUnit:
 * registrar cliente → solicitar cita → emitir ticket → consultar posición
 */
public class FlujoPrincipalTest {

    public static void main(String[] args) {
        // Setup
        GestorBancos    gestorBancos  = new GestorBancos();
        GestorClientes  gestorClientes = new GestorClientes();
        GestorCitas     gestorCitas   = new GestorCitas();
        GestorTickets   gestorTickets = new GestorTickets(gestorBancos);

        // Banco
        BancoServicio banco = new BancoServicio(1, "Banco Reclamos", "Sala A", 20, new ColaPrioridad());
        gestorBancos.agregarBanco(banco);

        // 1. Registrar cliente
        Cliente c = gestorClientes.registrarCliente("123456789", "Diego", "Lopez",
                "pass123", 25, "Calle 1", Cliente.TIPO_ESTANDAR);
        assert c != null : "FAIL: cliente no creado";
        System.out.println("✔ Cliente registrado: " + c.getNombreCompleto());

        // 2. Duplicado debe fallar
        Cliente dup = gestorClientes.registrarCliente("123456789", "Otro", "Apellido", "x", 30, "y", 0);
        assert dup == null : "FAIL: debió rechazar duplicado";
        System.out.println("✔ Duplicado rechazado correctamente");

        // 3. Solicitar cita
        Cita cita = gestorCitas.solicitarCita(c, "2026-05-10 09:00", "Sala A", Cita.TIPO_RECLAMO, "Producto defectuoso");
        assert cita != null : "FAIL: cita no creada";
        System.out.println("✔ Cita creada: " + cita.getId());

        // 4. Verificar cita vigente
        assert c.tieneCitaVigente(gestorCitas) : "FAIL: cliente debería tener cita vigente";
        System.out.println("✔ tieneCitaVigente() = true");

        // 5. Emitir ticket
        Ticket ticket = gestorTickets.emitirTicket(cita);
        assert ticket != null : "FAIL: ticket no emitido";
        System.out.println("✔ Ticket emitido: T" + ticket.getNumeroTurno() + " | Prioridad: " + ticket.getPrioridad());

        // 6. Consultar posición
        int pos = banco.obtenerPosicion(ticket.getId());
        assert pos == 1 : "FAIL: debería ser posición 1, fue " + pos;
        System.out.println("✔ Posición en cola: " + pos);

        // 7. History
        String[] logs = edu.co.diegoxs96.Server.Model.History.History.getInstance().getLogs();
        assert logs.length >= 2 : "FAIL: history no registró eventos";
        System.out.println("✔ History registró " + logs.length + " eventos");

        System.out.println("\n✅ Todos los tests pasaron.");
    }
}
