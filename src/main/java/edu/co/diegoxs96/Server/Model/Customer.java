package edu.co.diegoxs96.Server.Model;

import java.io.Serializable;

public class Customer implements Serializable, Comparable<Customer> {

    public enum ClientType { STANDARD, PREMIUM }

    private String     id;
    private String     names;
    private String     lastNames;
    private String     address;
    private int        age;
    private ClientType clientType;

    public Customer(String id, String names, String lastNames, String address, int age, ClientType clientType) {
        this.id         = id;
        this.names      = names;
        this.lastNames  = lastNames;
        this.address    = address;
        this.age        = age;
        this.clientType = clientType;
    }

    public String     getId()         { return id; }
    public String     getNames()      { return names; }
    public String     getLastNames()  { return lastNames; }
    public String     getAddress()    { return address; }
    public int        getAge()        { return age; }
    public ClientType getClientType() { return clientType; }

    /** Prioridad numérica: premium=2, mayor de 60=1 adicional, estándar=0 */
    public int getPriority() {
        int p = (clientType == ClientType.PREMIUM) ? 2 : 0;
        if (age > 60) p++;
        return p;
    }

    public void setNames(String names)         { this.names = names; }
    public void setLastNames(String lastNames) { this.lastNames = lastNames; }
    public void setAddress(String address)     { this.address = address; }

    @Override public int compareTo(Customer o) { return this.id.compareTo(o.id); }
    @Override public boolean equals(Object o)  { return o instanceof Customer && this.id.equals(((Customer)o).id); }
    @Override public int hashCode()            { return id.hashCode(); }
    @Override public String toString()         { return id + " - " + names + " " + lastNames; }
}
