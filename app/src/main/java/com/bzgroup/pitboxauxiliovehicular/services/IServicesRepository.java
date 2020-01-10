package com.bzgroup.pitboxauxiliovehicular.services;

public interface IServicesRepository {
    void handleMyVehicles();

    void handleServices(String categorieId);

    void handleMyAddress();

    void handleAddAddress(double latitude, double longitude, String description);
}
