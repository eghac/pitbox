package com.bzgroup.pitboxauxiliovehicular.addvehicle.events;

import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoCaja;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;

import java.util.List;

public class AddVehicleEvent {
    public static final int addVehicleSuccess = 10;
    public static final int addVehicleError = 11;

    public final static int getBrandsSuccess = 0;
    public final static int getVehiclesTypeSuccess = 1;
    public final static int getModelsSuccess = 2;
    public final static int getColorsSuccess = 3;
    public final static int getYearsSuccess = 4;

    public static final int modelsIsEmpty = 5;
    public static final int yearsIsEmpty = 6;
    public static final int colorsIsEmpty = 7;
    public static final int brandsIsEmpty = 8;
    public static final int vehiclesTypeIsEmpty = 9;
    public static final int editVehicleError = 12;
    public static final int editVehicleSuccess = 13;
    public static final int ADD_VEHICLE_BOX_TYPE_SUCCESS = 20;
    public static final int ADD_VEHICLE_TRANSMISSION_TYPE_SUCCESS = 21;
    public static final int ADD_VEHICLE_FUEL_TYPE_SUCCESS = 22;
    public static final int ADD_VEHICLE_ERROR = 23;
    public static final int ADD_VEHICLE_SUCESS = 24;

    private int eventType;
    private String errorMessage;
    private List<TipoVehiculo> mVehiclesType;
    private TipoCaja tipoCaja;
    private List<String> transmissionType;
    private List<String> fuelType;

    public List<String> getFuelType() {
        return fuelType;
    }

    public void setFuelType(List<String> fuelType) {
        this.fuelType = fuelType;
    }

    public List<String> getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(List<String> transmissionType) {
        this.transmissionType = transmissionType;
    }

    private Vehicle mVehicle;

    public Vehicle getVehicle() {
        return mVehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.mVehicle = vehicle;
    }

    public List<TipoVehiculo> getVehiclesType() {
        return mVehiclesType;
    }

    public void setVehiclesType(List<TipoVehiculo> vehiclesType) {
        this.mVehiclesType = vehiclesType;
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

    public void setBoxType(TipoCaja tipoCaja) {
        this.tipoCaja = tipoCaja;
    }

    public TipoCaja getTipoCaja() {
        return tipoCaja;
    }
}
