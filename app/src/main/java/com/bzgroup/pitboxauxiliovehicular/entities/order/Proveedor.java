package com.bzgroup.pitboxauxiliovehicular.entities.order;

public class Proveedor {
    private String id;
    private String nombre_perfil;
    private String latitud;
    private String longitud;
    private String foto;

    public Proveedor(String id, String nombre_perfil, String latitud, String longitud, String foto) {
        this.id = id;
        this.nombre_perfil = nombre_perfil;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id='" + id + '\'' +
                ", nombre_perfil='" + nombre_perfil + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_perfil() {
        return nombre_perfil;
    }

    public void setNombre_perfil(String nombre_perfil) {
        this.nombre_perfil = nombre_perfil;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
