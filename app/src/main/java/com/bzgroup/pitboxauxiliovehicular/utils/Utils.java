package com.bzgroup.pitboxauxiliovehicular.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    public static boolean isLogginGoogle(Context context) {
        GoogleSignInAccount googleSignInAccount;
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        return googleSignInAccount != null;
    }

    public static boolean isLogginFirebaseWithGoogle() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static void logoutInFirebase(Context context) {
        FirebaseAuth.getInstance().signOut();
        AppPreferences.getInstance(context).clearAll();
    }

    public static boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    public static void logoutInFacebook(Context context) {
        LoginManager.getInstance().logOut();
        AppPreferences.getInstance(context).clearAll();
    }

    public static boolean verifyInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobileNetState = connectivityManager.getNetworkInfo(0).getState();
        NetworkInfo.State wifiState = connectivityManager.getNetworkInfo(1).getState();

        if (wifiState == NetworkInfo.State.CONNECTED)
            return true;
        else if ((wifiState == NetworkInfo.State.DISCONNECTED || wifiState == NetworkInfo.State.UNKNOWN)
                && mobileNetState == NetworkInfo.State.CONNECTED)
            return true;
        else
            return false;
    }
}
