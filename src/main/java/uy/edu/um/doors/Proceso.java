package uy.edu.um.doors;

import uy.edu.um.tad.list.MyLinkedListImpl;
import uy.edu.um.tad.list.MyList;

public class Proceso implements Comparable<Proceso> {

    private int PID;
    private String nombre;
    private Usuario usuario;
    private int prioridad;
    private EstadoProceso estado;
    private MyList<Evento> eventos;
    private TipoFinalizacion tipoFinalizacion;

    public Proceso(int PID, String nombre, Usuario usuario) {
        this.PID = PID;
        this.nombre = nombre;
        this.usuario = usuario;
        this.estado = EstadoProceso.NEW;
        this.eventos = new MyLinkedListImpl<>();
    }

    public int getPID() {
        return PID;
    }

    public String getNombre() {
        return nombre;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public TipoFinalizacion getTipoFinalizacion() {
        return tipoFinalizacion;
    }

    public void setTipoFinalizacion(TipoFinalizacion tipoFinalizacion) {
        this.tipoFinalizacion = tipoFinalizacion;
    }


    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        this.estado = estado;
    }

    public MyList<Evento> getEventos() {
        return eventos;
    }

    @Override
    public int compareTo(Proceso otro) {
        return Integer.compare(otro.getPrioridad(), this.getPrioridad());
    }
}
