package com.bzgroup.pitboxauxiliovehicular.addvehicle.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoCaja;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;

import java.util.List;

public interface IAddVehicleView {
    void enableInputs();
    void disableInputs();
    void showProgress();
    void hideProgress();
    void providerVehiclesTypeSpinner(List<TipoVehiculo> vehiclesType);

    void addVehicleError(String errorMessage);

    void showMessage(String message);

    void navigateToMainScreen();

    void providerBoxType(TipoCaja boxType);

    void providerTrasnmissionType(List<String> transmissionType);

    void providerFuelType(List<String> fuelType);
//    void handleVehicleDescription(String description);
//    void onHandleDataModelsSpinnerIsEmpty();
//    void navigateToMainScreen();
//
//    void addVehicleError(String errorMessage);
//
//    void onHandleDataYearsSpinnerIsEmpty(String errorMessage);
//
//    void onHandleDataColorsSpinnerIsEmpty(String errorMessage);
//
//    void onHandleDataBrandSpinnerIsEmpty(String errorMessage);
//
//    void onHandleDataVehiclesTypeSpinnerIsEmpty(String errorMessage);
//
//    void showMessage(String message);
}
