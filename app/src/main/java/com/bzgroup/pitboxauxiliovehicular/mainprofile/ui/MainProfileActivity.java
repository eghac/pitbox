package com.bzgroup.pitboxauxiliovehicular.mainprofile.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.vehicles.ui.MyVehiclesActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.activity_main_profile_my_vehicles)
    public void handleMyVehicles() {
        startActivity(new Intent(this, MyVehiclesActivity.class));
    }

}
