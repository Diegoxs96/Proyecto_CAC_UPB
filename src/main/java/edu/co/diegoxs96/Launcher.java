package edu.co.diegoxs96;

/**
 * Clase de arranque separada de Application para evitar problemas
 * con el classpath de JavaFX al empaquetar con Maven.
 */
public class Launcher {
    public static void main(String[] args) {
        App.main(args);
    }
}
