package com.bzgroup.pitboxauxiliovehicular.myaddress;

import com.bzgroup.pitboxauxiliovehicular.myaddress.events.MyAddressEvent;

public interface IMyAddressPresenter {
    void onCreate();

    void onDestroy();

    void handleMyAddress();

    void onEventMainThread(MyAddressEvent event);

    void handleAddAddress(double latitude, double longitude, String description);
}
