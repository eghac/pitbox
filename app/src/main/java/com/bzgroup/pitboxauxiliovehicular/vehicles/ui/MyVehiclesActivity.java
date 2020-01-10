package com.bzgroup.pitboxauxiliovehicular.vehicles.ui;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;

import com.bzgroup.pitboxauxiliovehicular.R;

import butterknife.BindView;

public class MyVehiclesActivity extends AppCompatActivity implements VehiclesView{

    @BindView(R.id.activity_my_vehicles_progress)
    ProgressBar activity_my_vehicles_progress;
    @BindView(R.id.activity_my_vehicles_rv)
    RecyclerView activity_my_vehicles_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
