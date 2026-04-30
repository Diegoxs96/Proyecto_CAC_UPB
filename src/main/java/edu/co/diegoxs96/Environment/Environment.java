package edu.co.diegoxs96.Environment;

public class Environment {
    private String ip;
    private int port;
    private String serviceName;

    private static Environment instance;

    private Environment() {
        this.ip          = System.getProperty("server.ip",
                System.getenv().getOrDefault("SERVER_IP", "localhost"));
        this.port        = Integer.parseInt(
                System.getProperty("server.port",
                        System.getenv().getOrDefault("SERVER_PORT", "1099")));
        this.serviceName = System.getProperty("server.serviceName",
                System.getenv().getOrDefault("SERVICE_NAME", "TicketService"));
    }

    public static Environment getInstance() {
        if (instance == null) {
            instance = new Environment();
        }
        return instance;
    }

    public String getIp()          { return ip; }
    public int    getPort()        { return port; }
    public String getServiceName() { return serviceName; }
}