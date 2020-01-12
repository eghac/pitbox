package com.bzgroup.pitboxauxiliovehicular.entities.vehicle;

public class Vehicle {
    private String id;
    private String alias;
    private String placa;
    private String marca;
    private String modelo;
    private String submodelo;
    private String anho;
    private String tipo_caja;
    private String transmision;
    private String combustible;
    private String created_at;
    private String tipo_vehiculo_id;
    private String tipo_vehiculo_nombre;

    public Vehicle() {
    }

    public String getTipo_vehiculo_nombre() {
        return tipo_vehiculo_nombre;
    }

    public void setTipo_vehiculo_nombre(String tipo_vehiculo_nombre) {
        this.tipo_vehiculo_nombre = tipo_vehiculo_nombre;
    }

    public Vehicle(String id, String alias, String placa, String marca, String modelo, String submodelo, String anho, String tipo_caja, String transmision, String combustible, String created_at, String tipo_vehiculo_id, String tipo_vehiculo_nombre) {
        this.id = id;
        this.alias = alias;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.submodelo = submodelo;
        this.anho = anho;
        this.tipo_caja = tipo_caja;
        this.transmision = transmision;
        this.combustible = combustible;
        this.created_at = created_at;
        this.tipo_vehiculo_id = tipo_vehiculo_id;
        this.tipo_vehiculo_nombre = tipo_vehiculo_nombre;
    }

    @Override
    public String toString() {
        return alias + " " + placa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSubmodelo() {
        return submodelo;
    }

    public void setSubmodelo(String submodelo) {
        this.submodelo = submodelo;
    }

    public String getAnho() {
        return anho;
    }

    public void setAnho(String anho) {
        this.anho = anho;
    }

    public String getTipo_caja() {
        return tipo_caja;
    }

    public void setTipo_caja(String tipo_caja) {
        this.tipo_caja = tipo_caja;
    }

    public String getTransmision() {
        return transmision;
    }

    public void setTransmision(String transmision) {
        this.transmision = transmision;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTipo_vehiculo_id() {
        return tipo_vehiculo_id;
    }

    public void setTipo_vehiculo_id(String tipo_vehiculo_id) {
        this.tipo_vehiculo_id = tipo_vehiculo_id;
    }
}
