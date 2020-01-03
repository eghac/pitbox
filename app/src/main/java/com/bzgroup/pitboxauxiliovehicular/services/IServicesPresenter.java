package com.bzgroup.pitboxauxiliovehicular.services;

import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;

public interface IServicesPresenter {

    void onCreate();

    void onDestroy();

    void handleMyVehicles();

    void handleServices(String categorieId);

    void onEventMainThread(ServicesEvent services);
}
