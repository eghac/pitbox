package com.bzgroup.pitboxauxiliovehicular.accesstype.ui;

public interface IAccessTypeView {
    void showProgress();

    void hideProgress();

    void navigateToMainScreen();

    void navigateToSignInEmail();

    void handleGoogleSignIn();

    void handleFacebookSignIn();

    void showMessage(String message);

    void showErrorMessage(String errorMessage);
}
