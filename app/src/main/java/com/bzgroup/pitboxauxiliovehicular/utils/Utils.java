package com.bzgroup.pitboxauxiliovehicular.utils;

import android.content.Context;

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

    public static void logoutInFirebase() {
        FirebaseAuth.getInstance().signOut();
    }

    public static boolean isLoggedInFacebook() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    public static void logoutInFacebook(Context context) {
        LoginManager.getInstance().logOut();
//        AppPreferences.getInstance(context).clearAll();
    }
}
