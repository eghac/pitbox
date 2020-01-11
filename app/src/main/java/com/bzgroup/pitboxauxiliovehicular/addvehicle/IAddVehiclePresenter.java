package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import com.bzgroup.pitboxauxiliovehicular.addvehicle.events.AddVehicleEvent;

public interface IAddVehiclePresenter {
    void onCreate();

    void onDestroy();

    void handleVehiclesType();

    void onEventMainThread(AddVehicleEvent event);

    void handleBoxType();

    void handleTransmissionType();

    void handleFuelType();

    void handleAddVehicleConfirm(String alias, String licensePlate, int vehicleType, String brand, String model, String submodel, String year, String boxType, String transmissionType, String fuelType);
}
