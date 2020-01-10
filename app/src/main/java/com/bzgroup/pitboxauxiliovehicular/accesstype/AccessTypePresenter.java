package com.bzgroup.pitboxauxiliovehicular.accesstype;

import android.content.Context;

import com.bzgroup.pitboxauxiliovehicular.accesstype.event.AccessTypeEvent;
import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.AccessTypeActivity;
import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.IAccessTypeView;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

public class AccessTypePresenter implements IAccessTypePresenter {

    private IAccessTypeView mView;
    private IAccessTypeRepository mRepository;
    private EventBus eventBus;

    public AccessTypePresenter(Object object) {
        mView = (IAccessTypeView) object;
        eventBus = GreenRobotEventBus.getInstance();
        mRepository = new AccessTypeRepository((Context) object);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mView = null;
        eventBus.unregister(this);
    }

    @Override
    public void handleSignInFirebase(String uid, String email, String firstName, String lastName, String urlPhoto) {
        if (mView != null)
            mView.showProgress();
        mRepository.handleSignInFirebase(uid, email, firstName, lastName, urlPhoto);
    }

    @Subscribe
    @Override
    public void onEventMainThread(AccessTypeEvent accessTypeEvent) {
        switch (accessTypeEvent.getEventType()) {
            case AccessTypeEvent.ACCESS_TYPE_SUCCESS:
                accessTypeSuccess(accessTypeEvent.getErrorMessage());
                break;
            case AccessTypeEvent.ACCESS_TYPE_EMPTY:
                break;
            case AccessTypeEvent.ACCESS_TYPE_ERROR:
                accessTypeError(accessTypeEvent.getErrorMessage());
                break;
        }
    }

    private void accessTypeError(String errorMessage) {
        if (mView != null) {
            mView.hideProgress();
            mView.showErrorMessage(errorMessage);
        }
    }

    private void accessTypeSuccess(String message) {
        if (mView != null) {
            mView.hideProgress();
            mView.showMessage(message);
            mView.navigateToMainScreen();
        }
    }
}
