package com.bzgroup.pitboxauxiliovehicular.entities;

import java.io.Serializable;

public class Categorie implements Serializable {
    private String id;
    private  String nombre;
    private String image;
    private boolean programable;

    public Categorie(String id, String nombre, String image, boolean programable) {
        this.id = id;
        this.nombre = nombre;
        this.image = image;
        this.programable = programable;
    }

    public Categorie(String id, String nombre, String image) {
        this.id = id;
        this.nombre = nombre;
        this.image = image;
    }

    public boolean isProgramable() {
        return programable;
    }

    public void setProgramable(boolean programable) {
        this.programable = programable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
