package com.bzgroup.pitboxauxiliovehicular.entities;

public class Service {
    private String id;
    private String nombre;
    private String descripcion;
    private String precioBase;
    private String incrementoHorario;
    private String icono;

    public Service(String id, String nombre, String descripcion, String precioBase, String incrementoHorario, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioBase = precioBase;
        this.incrementoHorario = incrementoHorario;
        this.icono = icono;
    }

    @Override
    public String toString() {
        return nombre;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(String precioBase) {
        this.precioBase = precioBase;
    }

    public String getIncrementoHorario() {
        return incrementoHorario;
    }

    public void setIncrementoHorario(String incrementoHorario) {
        this.incrementoHorario = incrementoHorario;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
