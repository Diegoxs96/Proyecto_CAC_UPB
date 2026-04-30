package edu.co.diegoxs96.Server.Model.History;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una acción ejecutada en el servidor
 * (registrar ticket, atender, eliminar cliente, etc.).
 */
public class Action {

    private final String type;
    private final String description;
    private final String timestamp;

    public Action(String type, String description) {
        this.type        = type;
        this.description = description;
        this.timestamp   = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getType()        { return type; }
    public String getDescription() { return description; }
    public String getTimestamp()   { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + type + ": " + description;
    }
}
