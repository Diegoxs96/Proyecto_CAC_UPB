package edu.co.diegoxs96.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.co.diegoxs96.Server.Model.Ticket;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class TicketJson {

    private static final String PATH = "data/tickets.json";
    private static final Gson   GSON = new GsonBuilder().setPrettyPrinting().create();

    public TicketJson() {
        try { Files.createDirectories(Paths.get("data")); }
        catch (IOException e) { System.err.println("[JSON] No se pudo crear carpeta data/"); }
    }

    public void guardar(LinkedList<Ticket> tickets) {
        List<TicketDTO> dtos = new ArrayList<>();
        Iterator<Ticket> it = tickets.iterator();
        while (it.hasNext()) dtos.add(new TicketDTO(it.next()));

        try (Writer w = new FileWriter(PATH)) {
            GSON.toJson(dtos, w);
            System.out.println("[JSON] Tickets guardados: " + dtos.size());
        } catch (IOException e) {
            System.err.println("[JSON] Error guardando tickets: " + e.getMessage());
        }
    }

    public List<TicketDTO> cargar() {
        File file = new File(PATH);
        if (!file.exists()) return new ArrayList<>();

        try (Reader r = new FileReader(file)) {
            Type tipo = new TypeToken<List<TicketDTO>>() {}.getType();
            List<TicketDTO> dtos = GSON.fromJson(r, tipo);
            return dtos != null ? dtos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[JSON] Error cargando tickets: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
