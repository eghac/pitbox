package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import com.bzgroup.pitboxauxiliovehicular.addvehicle.events.AddVehicleEvent;

public interface IAddVehiclePresenter {
    void onCreate();

    void onDestroy();

    void handleVehiclesType();

    void onEventMainThread(AddVehicleEvent event);
}
