package edu.co.diegoxs96.Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.co.diegoxs96.Server.Model.Cliente;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteJson {

    private static final String PATH = "data/clientes.json";
    private static final Gson   GSON = new GsonBuilder().setPrettyPrinting().create();

    public ClienteJson() {
        try { Files.createDirectories(Paths.get("data")); }
        catch (IOException e) { System.err.println("[JSON] No se pudo crear carpeta data/"); }
    }

    public void guardar(LinkedList<Cliente> clientes) {
        List<ClienteDTO> dtos = new ArrayList<>();
        Iterator<Cliente> it = clientes.iterator();
        while (it.hasNext()) dtos.add(new ClienteDTO(it.next()));

        try (Writer w = new FileWriter(PATH)) {
            GSON.toJson(dtos, w);
            System.out.println("[JSON] Clientes guardados: " + dtos.size());
        } catch (IOException e) {
            System.err.println("[JSON] Error guardando clientes: " + e.getMessage());
        }
    }

    public List<ClienteDTO> cargar() {
        File file = new File(PATH);
        if (!file.exists()) return new ArrayList<>();

        try (Reader r = new FileReader(file)) {
            Type tipo = new TypeToken<List<ClienteDTO>>() {}.getType();
            List<ClienteDTO> dtos = GSON.fromJson(r, tipo);
            return dtos != null ? dtos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("[JSON] Error cargando clientes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
