package com.bzgroup.pitboxauxiliovehicular.accesstype;

import com.bzgroup.pitboxauxiliovehicular.accesstype.event.AccessTypeEvent;

public interface IAccessTypePresenter {
    void onCreate();

    void onDestroy();

    void handleSignInFirebase(String uid, String email, String firstName, String lastName, String urlPhoto);

    void onEventMainThread(AccessTypeEvent accessTypeEvent);
}
