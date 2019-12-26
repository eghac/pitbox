package com.bzgroup.pitboxauxiliovehicular.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bzgroup.pitboxauxiliovehicular.MainActivity;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.AccessTypeActivity;
import com.bzgroup.pitboxauxiliovehicular.utils.Utils;

public class SplashActiviy extends AppCompatActivity {

    private long splashTime = 1500;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activiy);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupSignIn();
    }

    private void setupSignIn() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSignIn();
            }
        }, splashTime);
    }

    private void handleSignIn() {
        if (Utils.isLogginFirebaseWithGoogle() || Utils.isLoggedInFacebook())
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, AccessTypeActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}
