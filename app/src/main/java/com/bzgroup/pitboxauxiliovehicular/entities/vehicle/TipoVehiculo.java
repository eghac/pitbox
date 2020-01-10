package com.bzgroup.pitboxauxiliovehicular.entities.vehicle;

import androidx.annotation.NonNull;

public class TipoVehiculo implements Comparable<TipoVehiculo> {
    private int id;
    private String nombre;

    public TipoVehiculo() {
    }

    public TipoVehiculo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoVehiculo) {
            TipoVehiculo vehicleType = (TipoVehiculo) obj;
            if (vehicleType.getNombre().equals(nombre) && vehicleType.getId() == id)
                return true;
        }
        return super.equals(obj);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int compareTo(@NonNull TipoVehiculo o) {
        return this.nombre.compareTo(o.nombre);
    }
}
