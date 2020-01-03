package com.bzgroup.pitboxauxiliovehicular.services.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;

import java.util.List;

public interface IServicesView {

    void showProgress();

    void hideProgress();

    void providerMyVehicles(List<Vehicle> myVehicles);

    void providerServices(List<Service> services);

    void providerServicesEmpty();

    void providerMyVehiclesEmpty();
}
