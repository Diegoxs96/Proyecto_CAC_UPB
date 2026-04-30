package edu.co.diegoxs96.Server.Model;

import java.io.Serializable;

public class Appointment implements Serializable, Comparable<Appointment> {

    public enum AppointmentType { RECLAMO, DEVOLUCION, ASESORIA }
    public enum AppointmentStatus { PENDING, COMPLETED, MISSED, CANCELLED }

    private String            id;
    private String            customerId;
    private AppointmentType   type;
    private AppointmentStatus status;
    private String            date;
    private String            time;
    private String            place;
    private String            description;

    public Appointment(String id, String customerId, AppointmentType type,
                       String date, String time, String place, String description) {
        this.id          = id;
        this.customerId  = customerId;
        this.type        = type;
        this.date        = date;
        this.time        = time;
        this.place       = place;
        this.description = description;
        this.status      = AppointmentStatus.PENDING;
    }

    public String            getId()          { return id; }
    public String            getCustomerId()  { return customerId; }
    public AppointmentType   getType()        { return type; }
    public AppointmentStatus getStatus()      { return status; }
    public String            getDate()        { return date; }
    public String            getTime()        { return time; }
    public String            getPlace()       { return place; }
    public String            getDescription() { return description; }

    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setDate(String date)                { this.date = date; }
    public void setTime(String time)                { this.time = time; }
    public void setPlace(String place)              { this.place = place; }
    public void setDescription(String desc)         { this.description = desc; }

    /** Comparación por fecha+hora para el árbol binario de búsqueda. */
    @Override public int compareTo(Appointment o) { return (date + time).compareTo(o.date + o.time); }
    @Override public boolean equals(Object o)     { return o instanceof Appointment && id.equals(((Appointment)o).id); }
    @Override public int hashCode()               { return id.hashCode(); }
    @Override public String toString()            { return id + " [" + type + " " + date + " " + time + "]"; }
}
