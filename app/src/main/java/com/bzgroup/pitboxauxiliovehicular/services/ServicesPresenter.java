package com.bzgroup.pitboxauxiliovehicular.services;

import android.content.Context;

import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.services.ui.IServicesView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class ServicesPresenter implements IServicesPresenter {
    private IServicesView mView;
    private IServicesRepository mRepository;
    private EventBus eventBus;

    public ServicesPresenter(Object context) {
        this.mView = mView;
        mView = (IServicesView) context;
        this.eventBus = GreenRobotEventBus.getInstance();
        mRepository = new ServicesRepository((Context) context);
    }


    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mView = null;
        eventBus.unregister(this);
    }

    @Override
    public void handleMyVehicles() {
        if (mView != null) {
            mView.showProgress();
        }
        mRepository.handleMyVehicles();
    }

    @Override
    public void handleServices(String categorieId) {
        if (mView != null) {
            mView.showProgress();
        }
        mRepository.handleServices(categorieId);
    }

    @Override
    public void handleAddAddress(double latitude, double longitude, String description) {
        if (mView != null)
            mView.showProgress();
        mRepository.handleAddAddress(latitude, longitude, description);
    }

    @Override
    public void handleMyAddress() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleMyAddress();
    }

    @Subscribe
    @Override
    public void onEventMainThread(ServicesEvent services) {
        switch (services.getEventType()) {
            case ServicesEvent.SERVICES_MYVEHICLES_SUCCESS:
                myVehiclesSuccess(services.getMyVehicles());
                break;
            case ServicesEvent.SERVICES_MYVEHICLES_EMPTY:
                servicesMyVehiclesEmpty(services.getErrorMessage());
                break;
            case ServicesEvent.SERVICES_MYVEHICLES_ERROR:
                break;
            case ServicesEvent.SERVICES_SUCCESS:
                servicesSucess(services.getServices());
                break;
            case ServicesEvent.SERVICES_EMPTY:
                break;
            case ServicesEvent.SERVICES_ERROR:
                break;
            case ServicesEvent.SERVICES_ADDRESS_EMPTY:
                servicesAddressEmpty(services.getErrorMessage());
                break;
            case ServicesEvent.SERVICES_ADDRESS_SUCCESS:
                addressSuccess(services.getAddresses());
                break;
            case ServicesEvent.SERVICES_ADD_ADDRESS_SUCCESS:
                addAddressSuccess(services.getErrorMessage());
                break;
            case ServicesEvent.SERVICES_ADD_ADDRESS_ERROR:
                addAddressError(services.getErrorMessage());
                break;
        }
    }

    private void addAddressError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.addAddressError(errorMessage);
        }
    }

    private void addAddressSuccess(String message) {
        if(mView != null) {
            mView.hideProgress();
            mView.addAddressSuccess(message);
        }
    }

    private void addressSuccess(List<Address> addresses) {
        if (mView != null) {
            mView.hideProgress();
            mView.hideContainerEmptyMyAddress();
            mView.providerAddress(addresses);
        }
    }

    private void servicesMyVehiclesEmpty(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerMyVehiclesEmpty(errorMessage);
        }
    }

    private void servicesAddressEmpty(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.showContainerEmptyMyAddress();
            mView.providerMyAddressIsEmpty(errorMessage);
        }
    }

    private void servicesSucess(List<Service> services) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerServices(services);
        }
    }

    private void myVehiclesSuccess(List<Vehicle> myVehicles) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerMyVehicles(myVehicles);
        }
    }
}
