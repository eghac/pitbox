package com.bzgroup.pitboxauxiliovehicular.accesstype;

public interface IAccessTypeRepository {
    void handleSignInFirebase(String uid,String email, String firstName, String lastName, String urlPhoto);
}
