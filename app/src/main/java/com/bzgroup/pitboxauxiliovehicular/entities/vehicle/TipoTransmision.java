package com.bzgroup.pitboxauxiliovehicular.entities.vehicle;

public class TipoTransmision {
    private String tt4x2;
    private String tt4x4;

    public TipoTransmision(String tt4x2, String tt4x4) {
        this.tt4x2 = tt4x2;
        this.tt4x4 = tt4x4;
    }

    public String getTt4x2() {
        return tt4x2;
    }

    public void setTt4x2(String tt4x2) {
        this.tt4x2 = tt4x2;
    }

    public String getTt4x4() {
        return tt4x4;
    }

    public void setTt4x4(String tt4x4) {
        this.tt4x4 = tt4x4;
    }
}
