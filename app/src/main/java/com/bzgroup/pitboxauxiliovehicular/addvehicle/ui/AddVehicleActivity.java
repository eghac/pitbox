package com.bzgroup.pitboxauxiliovehicular.addvehicle.ui;

import android.os.Bundle;

import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.IAccessTypeView;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.AddVehiclePresenter;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.IAddVehiclePresenter;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class AddVehicleActivity extends AppCompatActivity implements IAddVehicleView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.alias_et_extended_edit_text)
    ExtendedEditText alias_et_extended_edit_text;
    @BindView(R.id.plain_extended_edit_text)
    ExtendedEditText plain_extended_edit_text;
    @BindView(R.id.activity_add_vehicle_vehicle_type)
    Spinner activity_add_vehicle_vehicle_type;
    @BindView(R.id.brand_et_extended_edit_text)
    ExtendedEditText brand_et_extended_edit_text;
    @BindView(R.id.model_et_extended_edit_text)
    ExtendedEditText model_et_extended_edit_text;
    @BindView(R.id.submodel_et_extended_edit_text)
    ExtendedEditText submodel_et_extended_edit_text;
    @BindView(R.id.year_et_extended_edit_text)
    ExtendedEditText year_et_extended_edit_text;
    @BindView(R.id.activity_add_vehicle_vehicle_type_box)
    Spinner activity_add_vehicle_vehicle_type_box;
    @BindView(R.id.activity_add_vehicle_vehicle_transmission)
    Spinner activity_add_vehicle_vehicle_transmission;
    @BindView(R.id.activity_add_vehicle_vehicle_fuel_type)
    Spinner activity_add_vehicle_vehicle_fuel_type;

    private IAddVehiclePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setupSpinnerListeners();
//        setupInputs();

        mPresenter = new AddVehiclePresenter(this);
        mPresenter.onCreate();
        mPresenter.handleVehiclesType();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void setupInputs() {
//        activity_add_vehicle_plain.validate();
//        activity_add_vehicle_description.validate();
//    }

    private void setupSpinnerListeners() {
        activity_add_vehicle_vehicle_type.setOnItemSelectedListener(this);
    }

    @OnClick(R.id.activity_add_vehicle_vehicle_confirm_btn)
    public void handleAddVehicle(View view) {
        Toast.makeText(this, "Agregar vehículo", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void enableInputs() {

    }

    @Override
    public void disableInputs() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private ArrayAdapter<TipoVehiculo> adapterVehicleType;

    @Override
    public void providerVehiclesTypeSpinner(List<TipoVehiculo> vehiclesType) {
        adapterVehicleType = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_dropdown_item_eliot_onpressed, vehiclesType);
//        adapterVehicleType.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_eliot);
        activity_add_vehicle_vehicle_type.setAdapter(adapterVehicleType);
    }

    @Override
    public void addVehicleError(String errorMessage) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void navigateToMainScreen() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
