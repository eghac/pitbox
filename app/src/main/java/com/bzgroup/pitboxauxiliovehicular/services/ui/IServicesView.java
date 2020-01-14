package com.bzgroup.pitboxauxiliovehicular.services.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.order.Pedido;
import com.bzgroup.pitboxauxiliovehicular.entities.order.Proveedor;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;

import java.util.List;

public interface IServicesView {

    void showProgress();

    void hideProgress();

    void disabledInputs();

    void enabledInputs();

    void providerMyVehicles(List<Vehicle> myVehicles);

    void providerServices(List<Service> services);

    void providerServicesEmpty(String message);

    void providerMyVehiclesEmpty(String message);

    void providerMyAddressIsEmpty(String errorMessage);

    void providerAddress(List<Address> addresses);

    void showContainerEmptyMyAddress();

    void hideContainerEmptyMyAddress();

    void addAddressSuccess(String message);

    void addAddressError(String errorMessage);

    void showContainerOrder();

    void providerOrderSuccess(Pedido order, String message);

    void showOrderErrorMessage(String errorMessage);

    void hideServicesContainer();

    void showMessageSupplierAcceptedRequest(String supplierId, String message);

    void hideMessageSupplierAcceptedRequest(String message);

    void showContainerSupplierOnTheWay(Proveedor supplier);

    void showMessageGetLocationSupplierError(String errorMessage);

    void showClientAndSupplierLocationOnMap(Proveedor supplier);
}
