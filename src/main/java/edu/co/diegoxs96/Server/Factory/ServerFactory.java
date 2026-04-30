package edu.co.diegoxs96.Server.Factory;

import edu.co.diegoxs96.Environment.Environment;
import edu.co.diegoxs96.Server.Controller.ServerController;
import edu.co.diegoxs96.Server.Model.Server;
import edu.co.diegoxs96.Server.View.ServerView;

public class ServerFactory {
    public static ServerController create() {
        Environment env   = Environment.getInstance();
        Server      model = new Server(env.getIp(), env.getPort(), env.getServiceName());
        ServerView  view  = new ServerView("CAC-UPB — " + env.getServiceName());
        return new ServerController(model, view);
    }
}
