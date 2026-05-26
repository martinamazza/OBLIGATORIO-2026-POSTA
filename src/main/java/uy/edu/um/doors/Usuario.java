package uy.edu.um.doors;

public class Usuario {

    private int UID;
    private String alias;
    private TipoUsuario tipo;

    public Usuario (int UID, String alias, TipoUsuario tipo){
        this.UID = UID;
        this.alias = alias;
        this.tipo = tipo;
    }

    public int getUID() {
        return UID;
    }

    public String getAlias() {
        return alias;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }
}
