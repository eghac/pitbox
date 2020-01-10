package com.bzgroup.pitboxauxiliovehicular.myaddress;

public interface IMyAddressRepository {
    void handleMyAddress();

    void handleAddAddress(double latitude, double longitude, String description);
}
