package com.bzgroup.pitboxauxiliovehicular.vehicles.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;

import java.util.List;

public interface VehiclesView {
    void showProgress();

    void hideProgress();

    void showIsEmptyText(String message);

    void hideIsEmptyText();

    void providerMyVehicles(List<Vehicle> vehicles);
}
