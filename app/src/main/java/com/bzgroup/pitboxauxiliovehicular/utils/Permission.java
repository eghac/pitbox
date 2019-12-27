package com.bzgroup.pitboxauxiliovehicular.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class Permission {

    private static final String TAG = "Permission";

    private Activity activity;
//    Preferencias preferences;

    public Permission(Activity activity) {
        this.activity = activity;
//        preferences = new Preferencias(activity);
    }

    public static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;

//    private String[] locationPermissions = new String[]{
//                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
//                Manifest.permission.VIBRATE, Manifest.permission.ACCESS_COARSE_LOCATION,};

    private static String[] locationPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,};

    public static void requestLocationPermissionsBeforeDenied(Context context) {
        ActivityCompat.requestPermissions((Activity) context, locationPermissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
    }

    public static void requestLocationPermissions(Activity context) {
        int i = 0;
        for (; i < locationPermissions.length &&
                ActivityCompat.checkSelfPermission(context.getApplicationContext(), locationPermissions[i])
                        == PackageManager.PERMISSION_GRANTED; i++) {}

        if (i < locationPermissions.length)
            ActivityCompat.requestPermissions((Activity) context, locationPermissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
    }

    public static boolean validatePermissions(Context context, int[] grantResults) {
        boolean allGranted = false;
        //Revisa cada uno de los permisos y si estos fueron aceptados o no
        for (int i = 0; i < locationPermissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //Si todos los permisos fueron aceptados retorna true
//                preferences.setSolicitarPermisos(true);
                allGranted = true;
            } else {
                Log.d(TAG, "PERMISSION_DENIED");
                        //Si algun permiso no fue aceptado retorna false
                allGranted = false;
                break;
            }
        }
        return allGranted;
    }

    public static boolean validateLocationPermissions(Context context) {
        boolean allGranted = false;
        //Revisa cada uno de los permisos y si estos fueron aceptados o no
        for (int i = 0; i < locationPermissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(context, locationPermissions[i]) == PackageManager.PERMISSION_GRANTED) {

                //Si todos los permisos fueron aceptados retorna true
//                preferences.setSolicitarPermisos(true);
                allGranted = true;
            } else {
                //Si algun permiso no fue aceptado retorna false
                allGranted = false;
                break;
            }
        }
        return allGranted;
    }
}
