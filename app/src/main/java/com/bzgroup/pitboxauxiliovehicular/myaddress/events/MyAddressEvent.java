package com.bzgroup.pitboxauxiliovehicular.myaddress.events;

import com.bzgroup.pitboxauxiliovehicular.entities.Address;

import java.util.List;

public class MyAddressEvent {
    public final static int MY_ADDRESS_SUCCESS = 0;
    public final static int MY_ADDRESS_EMPTY = 1;
    public final static int MY_ADDRESS_ERROR = 2;

    public final static int MY_ADD_ADDRESS_SUCCESS = 3;
    public final static int MY_ADD_ADDRESS_EMPTY = 4;
    public final static int MY_ADD_ADDRESS_ERROR = 5;

    private int eventType;
    private String errorMessage;
    private List<Address> addresses;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
