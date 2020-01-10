package com.bzgroup.pitboxauxiliovehicular.vehicles;

import com.bzgroup.pitboxauxiliovehicular.vehicles.events.MyVehiclesEvent;

public interface IVehiclesPresenter {
    void onCreate();
    void onDestroy();
    void handleMyVehicles();
    void onEventMainThread(MyVehiclesEvent event);
}
