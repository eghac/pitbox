package com.bzgroup.pitboxauxiliovehicular.entities.vehicle;

public class TipoCaja {
    private String automatica;
    private String mecanica;

    public TipoCaja(String automatica, String mecanica) {
        this.automatica = automatica;
        this.mecanica = mecanica;
    }

    public String getAutomatica() {
        return automatica;
    }

    public void setAutomatica(String automatica) {
        this.automatica = automatica;
    }

    public String getMecanica() {
        return mecanica;
    }

    public void setMecanica(String mecanica) {
        this.mecanica = mecanica;
    }
}
