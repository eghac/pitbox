package com.bzgroup.pitboxauxiliovehicular.myaddress.ui;

import com.bzgroup.pitboxauxiliovehicular.entities.Address;

import java.util.List;

public interface IMyAddressView {
    void showProgress();

    void hideProgress();

    void providerMyAddress(List<Address> addresses);

    void addAddressSuccess(String message);

    void showAddAddressError(String errorMessage);

    void showMessageAddressEmpty(String message);
}
