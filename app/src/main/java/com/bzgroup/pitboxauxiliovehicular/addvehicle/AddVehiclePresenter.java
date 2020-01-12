package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import android.graphics.Bitmap;

import com.bzgroup.pitboxauxiliovehicular.addvehicle.events.AddVehicleEvent;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.ui.IAddVehicleView;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoCaja;
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
        if (mView != null)
            mView.showProgress();
        mRepository.handleVehiclesType();
    }

    @Override
    public void handleBoxType() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleBoxType();
    }

    @Override
    public void handleTransmissionType() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleTransmissionType();
    }

    @Override
    public void handleFuelType() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleFuelType();
    }

    @Override
    public void handleAddVehicleConfirm(String alias, String licensePlate, int vehicleType, String brand, String model, String submodel, String year, String boxType, String transmissionType, String fuelType) {
        if (mView != null) {
            mView.showProgress();
            mView.disableInputs();
        }
        mRepository.handleAddVehicleConfirm(alias, licensePlate, vehicleType, brand, model, submodel, year, boxType, transmissionType, fuelType);
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
            case AddVehicleEvent.ADD_VEHICLE_BOX_TYPE_SUCCESS:
                boxTypeSuccess(event.getTipoCaja());
                break;
            case AddVehicleEvent.ADD_VEHICLE_TRANSMISSION_TYPE_SUCCESS:
                transmisstionTypeSucess(event.getTransmissionType());
                break;
            case AddVehicleEvent.ADD_VEHICLE_FUEL_TYPE_SUCCESS:
                fuelTypeSuccess(event.getFuelType());
                break;
            case AddVehicleEvent.ADD_VEHICLE_SUCESS:
                addVehicleSuccess(event.getErrorMessage());
                break;
            case AddVehicleEvent.ADD_VEHICLE_ERROR:
                addVehicleError(event.getErrorMessage());
                break;
        }
    }

    private void fuelTypeSuccess(List<String> fuelType) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerFuelType(fuelType);
        }
    }

    private void transmisstionTypeSucess(List<String> transmissionType) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerTrasnmissionType(transmissionType);
        }
    }

    private void boxTypeSuccess(TipoCaja tipoCaja) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerBoxType(tipoCaja);
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

    private void addVehicleSuccess(String message) {
        if (mView != null) {
            mView.showMessage(message);
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
