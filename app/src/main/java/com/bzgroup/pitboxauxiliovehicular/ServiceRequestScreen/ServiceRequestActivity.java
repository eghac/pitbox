package com.bzgroup.pitboxauxiliovehicular.ServiceRequestScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bzgroup.pitboxauxiliovehicular.MainActivity;
import com.bzgroup.pitboxauxiliovehicular.R;

public class ServiceRequestActivity extends AppCompatActivity {

    private long splashTime = 2500;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_request);
        method();
    }

    private void method() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ServiceRequestActivity.this, MainActivity.class));
                finish();
            }
        }, splashTime);
    }
}
