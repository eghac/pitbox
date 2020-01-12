package com.bzgroup.pitboxauxiliovehicular.entities.order;

public class Pedido {
    private String id;
    private String cliente_id;
    private String vehiculo_id;
    private String servicio_id;
    private String latitud;
    private String longitud;
    private String fecha_programacion;
    private String hora_programacion;
    private String estado;
    private String descripcion;
    private String fecha_inicio;
    private String hora_inicio;
    private String precio_base;
    private String precio_total;
    private String created_at;
    private String updated_at;

    public Pedido(String id, String cliente_id, String vehiculo_id, String servicio_id, String latitud, String longitud, String fecha_programacion, String hora_programacion, String estado, String descripcion, String fecha_inicio, String hora_inicio, String precio_base, String precio_total, String created_at, String updated_at) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.vehiculo_id = vehiculo_id;
        this.servicio_id = servicio_id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha_programacion = fecha_programacion;
        this.hora_programacion = hora_programacion;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.hora_inicio = hora_inicio;
        this.precio_base = precio_base;
        this.precio_total = precio_total;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public String getVehiculo_id() {
        return vehiculo_id;
    }

    public void setVehiculo_id(String vehiculo_id) {
        this.vehiculo_id = vehiculo_id;
    }

    public String getServicio_id() {
        return servicio_id;
    }

    public void setServicio_id(String servicio_id) {
        this.servicio_id = servicio_id;
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

    public String getFecha_programacion() {
        return fecha_programacion;
    }

    public void setFecha_programacion(String fecha_programacion) {
        this.fecha_programacion = fecha_programacion;
    }

    public String getHora_programacion() {
        return hora_programacion;
    }

    public void setHora_programacion(String hora_programacion) {
        this.hora_programacion = hora_programacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getPrecio_base() {
        return precio_base;
    }

    public void setPrecio_base(String precio_base) {
        this.precio_base = precio_base;
    }

    public String getPrecio_total() {
        return precio_total;
    }

    public void setPrecio_total(String precio_total) {
        this.precio_total = precio_total;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
