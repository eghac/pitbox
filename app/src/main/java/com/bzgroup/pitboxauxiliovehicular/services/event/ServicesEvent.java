package com.bzgroup.pitboxauxiliovehicular.services.event;

import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;

import java.util.List;

public class ServicesEvent {
    public final static int SERVICES_MYVEHICLES_SUCCESS = 0;
    public final static int SERVICES_MYVEHICLES_EMPTY = 1;
    public final static int SERVICES_MYVEHICLES_ERROR = 2;

    public final static int SERVICES_SUCCESS = 3;
    public final static int SERVICES_EMPTY = 4;
    public final static int SERVICES_ERROR = 5;

    private int eventType;
    private String errorMessage;
    private List<Vehicle> myVehicles;
    private List<Service> services;

    public List<Vehicle> getMyVehicles() {
        return myVehicles;
    }

    public void setMyVehicles(List<Vehicle> myVehicles) {
        this.myVehicles = myVehicles;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
