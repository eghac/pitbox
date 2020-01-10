package com.bzgroup.pitboxauxiliovehicular.vehicles.events;

import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;

import java.util.List;

public class MyVehiclesEvent {
    public final static int showMyVehiclesError = 0;
    public final static int showMyVehiclesSuccess = 1;
    public final static int myVehiclesIsEmpty = 2;

    private int eventType;
    private String errorMesage;
    private List<Vehicle> vehicles;

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }
}
