package edu.co.diegoxs96.Server.Model.History;

import edu.co.diegoxs96.Server.Model.observer.Observer;
import edu.co.diegoxs96.Server.Model.observer.Subject;
import edu.co.diegoxs96.structures.linkedlist.singly.LinkedList;
import edu.co.diegoxs96.structures.stack.StackList;
import edu.co.diegoxs96.structures.model.iterator.Iterator;

import java.util.ArrayList;
import java.util.List;

public class History extends Subject {

    private final LinkedList<String> log      = new LinkedList<>();
    private final StackList<Action>  actions  = new StackList<>();
    private final List<Observer>     observers = new ArrayList<>();

    // Singleton para acceso global
    private static History instance;
    public static History getInstance() {
        if (instance == null) instance = new History();
        return instance;
    }

    @Override public void attach(Observer o)  { observers.add(o); }
    @Override public void detach(Observer o)  { observers.remove(o); }
    @Override public void notifyObservers()   { observers.forEach(Observer::update); }

    public void record(String type, String description) {
        Action a = new Action(type, description);
        actions.push(a);
        log.add(a.toString());
        notifyObservers();
    }

    public Action undo() {
        return actions.pop();
    }
    public String[] getLogs() {
        Object[] raw = log.toArray();
        String[] result = new String[raw.length];
        for (int i = 0; i < raw.length; i++) {
            result[i] = (String) raw[i];
        }
        return result;
    }
    public int actionCount()  {
        return actions.size();
    }

    public int logCount()    {
        return log.size();
    }
}
