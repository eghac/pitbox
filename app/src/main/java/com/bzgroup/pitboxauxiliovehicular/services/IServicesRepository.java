package com.bzgroup.pitboxauxiliovehicular.services;

public interface IServicesRepository {
    void handleMyVehicles();

    void handleServices(String categorieId);

    void handleMyAddress();

    void handleAddAddress(double latitude, double longitude, String description);

    void handleOrder(String vehicleId, String serviceId, double latitude, double longitude, String scheduleDate, String scheduleTime, String description);

    void handleGetSupplier(String orderId);

    void handleSupplierLocation(String supplierId);
}
