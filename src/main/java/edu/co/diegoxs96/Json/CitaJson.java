package edu.co.diegoxs96.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.co.diegoxs96.Server.Model.Cita;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CitaJson {

    private static final String PATH = "data/citas.json";
    private static final Gson   GSON = new GsonBuilder().setPrettyPrinting().create();

    public CitaJson() {
        try { Files.createDirectories(Paths.get("data")); }
        catch (IOException e) { System.err.println("[JSON] No se pudo crear carpeta data/"); }
    }

    public void guardar(LinkedList<Cita> citas) {
        List<CitaDTO> dtos = new ArrayList<>();
        Iterator<Cita> it = citas.iterator();
        while (it.hasNext()) dtos.add(new CitaDTO(it.next()));

        try (Writer w = new FileWriter(PATH)) {
            GSON.toJson(dtos, w);
            System.out.println("[JSON] Citas guardadas: " + dtos.size());
        } catch (IOException e) {
            System.err.println("[JSON] Error guardando citas: " + e.getMessage());
        }
    }

    /**
     * Carga las citas desde JSON y reconstruye cada Cita con su Cliente.
     * Necesita la lista de clientes para buscar el cliente por id.
     */
    public List<Cita> cargar(LinkedList<Cliente> clientes) {
        File file = new File(PATH);
        if (!file.exists()) return new ArrayList<>();

        try (Reader r = new FileReader(file)) {
            Type tipo = new TypeToken<List<CitaRawDTO>>() {}.getType();
            List<CitaRawDTO> raws = GSON.fromJson(r, tipo);
            if (raws == null) return new ArrayList<>();

            List<Cita> result = new ArrayList<>();
            for (CitaRawDTO raw : raws) {
                Cliente cliente = buscarCliente(clientes, raw.clienteId);
                if (cliente == null) continue;
                String fechaHora = raw.fecha + "T" + raw.hora;
                Cita c = new Cita(raw.id, cliente, fechaHora, raw.banco,
                        tipoCodigo(raw.tipo), estadoCodigo(raw.estado));
                result.add(c);
            }
            System.out.println("[JSON] Citas cargadas: " + result.size());
            return result;
        } catch (IOException e) {
            System.err.println("[JSON] Error cargando citas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private Cliente buscarCliente(LinkedList<Cliente> clientes, int id) {
        Iterator<Cliente> it = clientes.iterator();
        while (it.hasNext()) {
            Cliente c = it.next();
            if (c.getId() == id) return c;
        }
        return null;
    }

    private int tipoCodigo(String tipo) {
        return switch (tipo) {
            case "Reclamo"    -> 0;
            case "Devolución" -> 1;
            case "Asesoría"   -> 2;
            default           -> 0;
        };
    }

    // DTO interno para cargar — incluye clienteId para reconstruir la relación
    private static class CitaRawDTO {
        int    id;
        int    clienteId;
        String tipo;
        String fecha;
        String hora;
        String banco;
        String estado;  // viene como texto: "Pendiente", "Completada", etc.
    }

    private int estadoCodigo(String estado) {
        if (estado == null) return 0;
        return switch (estado) {
            case "Completada"   -> 1;
            case "No asistida"  -> 2;
            case "Cancelada"    -> 3;
            default             -> 0; // Pendiente
        };
    }
}