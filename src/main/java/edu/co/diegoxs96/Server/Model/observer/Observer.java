package edu.co.diegoxs96.Server.Model.observer;

import edu.co.diegoxs96.Server.Model.Server;

public abstract class Observer {
    protected Subject subject;

    protected Observer(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    public abstract void update();

}
