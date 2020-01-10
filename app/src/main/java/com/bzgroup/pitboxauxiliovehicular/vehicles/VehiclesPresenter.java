package com.bzgroup.pitboxauxiliovehicular.vehicles;

import android.content.Context;

import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.vehicles.events.MyVehiclesEvent;
import com.bzgroup.pitboxauxiliovehicular.vehicles.ui.VehiclesView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class VehiclesPresenter implements IVehiclesPresenter {

    private EventBus mEventBus;
    private VehiclesView mView;
    private IVehiclesRepository mRepository;

    public VehiclesPresenter(Object context) {
        mEventBus = GreenRobotEventBus.getInstance();
        mView = (VehiclesView) context;
        mRepository = new VehiclesRepository((Context) context);
    }

    @Override
    public void onCreate() {
        mEventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mView = null;
        mEventBus.unregister(this);
    }

    @Override
    public void handleMyVehicles() {
        if (mView != null) {
//            mView.showIsEmptyText();
            mView.showProgress();
        }
        mRepository.handleMyVehicles();
    }

    @Subscribe
    @Override
    public void onEventMainThread(MyVehiclesEvent event) {
        switch (event.getEventType()) {
            case MyVehiclesEvent.showMyVehiclesSuccess:
                showMyVehiclesSuccess(event.getVehicles());
                break;
            case MyVehiclesEvent.myVehiclesIsEmpty:
                showMyVehiclesSuccessIsEmpty(event.getErrorMesage());
                break;
        }
    }

    private void showMyVehiclesSuccess(List<Vehicle> vehicles) {
        if (mView != null) {
            mView.hideIsEmptyText();
            mView.hideProgress();
            mView.providerMyVehicles(vehicles);
        }
    }

    private void showMyVehiclesSuccessIsEmpty(String message) {
        if (mView != null) {
            mView.showIsEmptyText(message);
            mView.hideProgress();
        }
    }
}
