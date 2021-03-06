package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import android.graphics.Bitmap;

public interface IAddVehicleRepository {
//    void handleOnSpinnerBrands();
//
//    void handleOnSpinnerVehiclesType(int brandId);
//
//    void handleOnSpinnerModels(int vehicleTypeId);
//
//    void handleOnSpinnerColors(int modelId);
//
//    void handleOnSpinnerYears(int modelId, int colorId);
//
//    void handleOnSpinnerSetYears(int modelId, int yearId);
//
//    void handleAddVehicleConfirm(String plain, String vin, String description, Bitmap mBitmap);
//
//    void handleAddVehicleEditConfirm(int idVehiculoServer, String plain, String vin, String description, Bitmap mBitmap);

    void handleVehiclesType();

    void handleBoxType();

    void handleTransmissionType();

    void handleFuelType();

    void handleAddVehicleConfirm(String alias, String licensePlate, int vehicleType, String brand, String model, String submodel, String year, String boxType, String transmissionType, String fuelType);
}
