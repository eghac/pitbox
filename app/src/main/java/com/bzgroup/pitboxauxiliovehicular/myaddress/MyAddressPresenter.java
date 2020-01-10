package com.bzgroup.pitboxauxiliovehicular.myaddress;

import android.content.Context;

import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.myaddress.events.MyAddressEvent;
import com.bzgroup.pitboxauxiliovehicular.myaddress.ui.IMyAddressView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class MyAddressPresenter implements IMyAddressPresenter {

    private IMyAddressView mView;
    private IMyAddressRepository mRepository;
    private EventBus mEventBus;

    public MyAddressPresenter(Object object) {
        this.mView = (IMyAddressView) object;
        mEventBus = GreenRobotEventBus.getInstance();
        mRepository = new MyAddressRepository((Context) object);
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
    public void handleAddAddress(double latitude, double longitude, String description) {
        if (mView != null)
            mView.showProgress();
        mRepository.handleAddAddress(latitude, longitude, description);
    }

    @Override
    public void handleMyAddress() {
        if (mView != null)
            mView.showProgress();
        mRepository.handleMyAddress();
    }

    @Subscribe
    @Override
    public void onEventMainThread(MyAddressEvent event) {
        switch (event.getEventType()) {
            case MyAddressEvent.MY_ADDRESS_SUCCESS:
                myAddressSuccess(event.getAddresses());
                break;
            case MyAddressEvent.MY_ADDRESS_ERROR:
                myAddressError(event.getErrorMessage());
                break;
            case MyAddressEvent.MY_ADDRESS_EMPTY:
                myAddressEmpty(event.getErrorMessage());
                break;
            case MyAddressEvent.MY_ADD_ADDRESS_SUCCESS:
                myAddAddressSuccess(event.getErrorMessage());
                break;
            case MyAddressEvent.MY_ADD_ADDRESS_ERROR:
                myAddAddressError(event.getErrorMessage());
                break;
        }
    }

    private void myAddAddressError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.showAddAddressError(errorMessage);
        }
    }

    private void myAddAddressSuccess(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.addAddressSuccess(message);
        }
    }

    private void myAddressError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.showAddAddressError(errorMessage);
        }
    }

    private void myAddressEmpty(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showMessageAddressEmpty(message);
        }
    }

    private void myAddressSuccess(List<Address> addresses) {
        if (mView != null) {
            mView.hideProgress();
            mView.providerMyAddress(addresses);
        }
    }
}
