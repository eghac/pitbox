package com.bzgroup.pitboxauxiliovehicular.services;

import android.content.Context;

import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.services.ui.IServicesView;
import com.google.android.gms.dynamic.IFragmentWrapper;

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

    @Subscribe
    @Override
    public void onEventMainThread(ServicesEvent services) {
        switch (services.getEventType()) {
            case ServicesEvent.SERVICES_MYVEHICLES_SUCCESS:
                myVehiclesSuccess(services.getMyVehicles());
                break;
            case ServicesEvent.SERVICES_MYVEHICLES_EMPTY:
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
