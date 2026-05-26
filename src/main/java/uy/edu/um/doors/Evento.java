package uy.edu.um.doors;

import uy.edu.um.tad.list.MyLinkedListImpl;
import uy.edu.um.tad.list.MyList;

public class Evento {

    private TipoEvento tipo;
    private MyList<String> instrucciones;

    public Evento(TipoEvento tipo) {
        this.tipo = tipo;
        this.instrucciones = new MyLinkedListImpl<>();
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public MyList<String> getInstrucciones() {
        return instrucciones;
    }
}
