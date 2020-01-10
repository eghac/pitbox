package com.bzgroup.pitboxauxiliovehicular.vehicles.ui;

import android.content.Intent;
import android.os.Bundle;

import com.bzgroup.pitboxauxiliovehicular.addvehicle.ui.AddVehicleActivity;
import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.vehicles.IVehiclesPresenter;
import com.bzgroup.pitboxauxiliovehicular.vehicles.VehiclesPresenter;
import com.bzgroup.pitboxauxiliovehicular.vehicles.adapter.MyVehiclesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bzgroup.pitboxauxiliovehicular.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyVehiclesActivity extends AppCompatActivity implements VehiclesView {

    @BindView(R.id.activity_my_vehicles_progress)
    ProgressBar activity_my_vehicles_progress;
    @BindView(R.id.activity_my_vehicles_rv)
    RecyclerView activity_my_vehicles_rv;
    private MyVehiclesAdapter mAdapter;
    @BindView(R.id.activity_my_vehicles_add_btn)
    Button activity_my_vehicles_add_btn;
    @BindView(R.id.activity_my_vehicles_is_empty_container)
    ConstraintLayout activity_my_vehicles_is_empty_container;
    @BindView(R.id.activity_my_vehicles_is_empty_txt)
    TextView activity_my_vehicles_is_empty_txt;

    private IVehiclesPresenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setupAdapter();
        setupRecycler();
        handleAnimation();
        mPresenter = new VehiclesPresenter(this);
        mPresenter.onCreate();
        mPresenter.handleMyVehicles();
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                startActivity(new Intent(MyVehiclesActivity.this, AddVehicleActivity.class));
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupAdapter() {

        List<Vehicle> vehicles = new ArrayList<>();
//        vehicles.add(new Vehicle());
//        vehicles.add(new Vehicle());
//        vehicles.add(new Vehicle());
//        vehicles.add(new Vehicle());
        mAdapter = new MyVehiclesAdapter(this, vehicles);
    }

    private void setupRecycler() {
        activity_my_vehicles_rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activity_my_vehicles_rv.setLayoutManager(layoutManager);
        activity_my_vehicles_rv.setAdapter(mAdapter);
    }

    @OnClick(R.id.activity_my_vehicles_add_btn)
    public void handleAddVehicle() {
        startActivity(new Intent(MyVehiclesActivity.this, AddVehicleActivity.class));
    }

    @Override
    public void showProgress() {
        activity_my_vehicles_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity_my_vehicles_progress.setVisibility(View.GONE);
    }

    @Override
    public void showIsEmptyText(String message) {
        activity_my_vehicles_rv.setVisibility(View.GONE);
        activity_my_vehicles_is_empty_container.setVisibility(View.VISIBLE);
        activity_my_vehicles_is_empty_txt.setText(message);
    }

    @Override
    public void hideIsEmptyText() {
        activity_my_vehicles_is_empty_container.setVisibility(View.VISIBLE);
        activity_my_vehicles_rv.setVisibility(View.GONE);
    }

    private void handleAnimation() {
        activity_my_vehicles_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE)
//                    fragment_myvehicles_add_fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && activity_my_vehicles_add_btn.isShown()) {
//                    fragment_myvehicles_add_fab.hide();
                    Animation shake = AnimationUtils.loadAnimation(MyVehiclesActivity.this, R.anim.fade_out_button);
                    activity_my_vehicles_add_btn.startAnimation(shake);
                    activity_my_vehicles_add_btn.setVisibility(View.GONE);
                } else if (dy < 0 && !activity_my_vehicles_add_btn.isShown()) {
//                    fragment_myvehicles_add_fab.show();
                    Animation shake = AnimationUtils.loadAnimation(MyVehiclesActivity.this, R.anim.fade_in_button);
                    activity_my_vehicles_add_btn.startAnimation(shake);
                    activity_my_vehicles_add_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}
