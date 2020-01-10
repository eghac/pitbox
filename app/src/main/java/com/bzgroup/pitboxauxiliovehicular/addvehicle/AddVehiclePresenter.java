package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import android.graphics.Bitmap;

import com.bzgroup.pitboxauxiliovehicular.addvehicle.events.AddVehicleEvent;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.ui.IAddVehicleView;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class AddVehiclePresenter implements IAddVehiclePresenter {

    private EventBus mEventBus;
    private IAddVehicleView mView;
    private IAddVehicleRepository mRepository;

    public AddVehiclePresenter(Object context) {
        mEventBus = GreenRobotEventBus.getInstance();
        mView = (IAddVehicleView) context;
        mRepository = new AddVehicleRepository(context);
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
    public void handleVehiclesType() {
        mRepository.handleVehiclesType();
    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(AddVehicleEvent event) {
        switch (event.getEventType()) {
            case AddVehicleEvent.getVehiclesTypeSuccess:
                vehicleTypeSucess(event.getVehiclesType());
                break;
            case AddVehicleEvent.vehiclesTypeIsEmpty:
                break;
        }
    }

    private void vehicleTypeSucess(List<TipoVehiculo> vehiclesType) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerVehiclesTypeSpinner(vehiclesType);
        }
    }

    private void editVehicleError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.enableInputs();
            mView.addVehicleError(errorMessage);
        }
    }

    private void addVehicleError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.enableInputs();
            mView.addVehicleError(errorMessage);
        }
    }

    private void editVehicleSuccess() {
        if (mView != null) {
            mView.showMessage("Vehículo actualizado exitósamente.");
            mView.navigateToMainScreen();
        }
    }

    private void addVehicleSuccess() {
        if (mView != null) {
            mView.showMessage("Vehículo agregado exitósamente");
            mView.navigateToMainScreen();
        }
    }


//    private void setDataVehiclesTypeSpinner(List<TipoVehiculo> vehiclesType) {
//        if (mView != null)
//            mView.onHandleDataVehiclesTypeSpinner(vehiclesType);
//    }
//
//    private void vehiclesTypeIsEmpty(String errorMessage) {
//        if (mView != null)
//            mView.onHandleDataVehiclesTypeSpinnerIsEmpty(errorMessage);
//    }

}
