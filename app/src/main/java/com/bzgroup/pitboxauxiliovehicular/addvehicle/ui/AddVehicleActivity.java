package com.bzgroup.pitboxauxiliovehicular.addvehicle.ui;

import android.content.Intent;
import android.os.Bundle;

import com.bzgroup.pitboxauxiliovehicular.accesstype.ui.IAccessTypeView;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.AddVehiclePresenter;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.IAddVehiclePresenter;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.dialog.ConfirmAddVehicleDialogFragment;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoCaja;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;
import com.bzgroup.pitboxauxiliovehicular.vehicles.ui.MyVehiclesActivity;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.text.TextPaint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class AddVehicleActivity extends AppCompatActivity implements IAddVehicleView, AdapterView.OnItemSelectedListener, ConfirmAddVehicleDialogFragment.ConfirmAddVehicleDialogFragmentListener {

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
    @BindView(R.id.activity_add_vehicle_progress)
    ProgressBar activity_add_vehicle_progress;
    @BindView(R.id.activity_add_vehicle_vehicle_confirm_btn)
    Button activity_add_vehicle_vehicle_confirm_btn;

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
        mPresenter.handleBoxType();
        mPresenter.handleTransmissionType();
        mPresenter.handleFuelType();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void setupInputs() {
//        activity_add_vehicle_plain.validate();
//        activity_add_vehicle_description.validate();
//    }

    private void setupSpinnerListeners() {
        activity_add_vehicle_vehicle_type.setOnItemSelectedListener(this);
        activity_add_vehicle_vehicle_type_box.setOnItemSelectedListener(this);
        activity_add_vehicle_vehicle_transmission.setOnItemSelectedListener(this);
        activity_add_vehicle_vehicle_fuel_type.setOnItemSelectedListener(this);
    }

    private int vehicleType = -1;
    private String boxType;
    private String transmissionType;
    private String fuelType;

    private String alias;
    private String licensePlate;
    private String brand;
    private String model;
    private String submodel;
    private String year;

    @OnClick(R.id.activity_add_vehicle_vehicle_confirm_btn)
    public void handleAddVehicle(View view) {
        alias = alias_et_extended_edit_text.getText().toString();
        licensePlate = plain_extended_edit_text.getText().toString();
        brand = brand_et_extended_edit_text.getText().toString();
        model = model_et_extended_edit_text.getText().toString();
        submodel = submodel_et_extended_edit_text.getText().toString();
        year = year_et_extended_edit_text.getText().toString();
        String message = "";
        if (alias == null || alias.isEmpty()) {
            message = "Introducir un ALIAS";
            showMessageAddVehicle(message);
        } else if (licensePlate == null || licensePlate.isEmpty()) {
            message = "Introducir la PLACA";
            showMessageAddVehicle(message);
        } else if (vehicleType == -1) {
            message = "Introducir TIPO DE VEHICULO";
            showMessageAddVehicle(message);
        } else if (brand == null || brand.isEmpty()) {
            message = "Introducir MARCA";
            showMessageAddVehicle(message);
        } else if (model == null || model.isEmpty()) {
            message = "Introducir MODELO";
            showMessageAddVehicle(message);
        }
//        else if (submodel == null || submodel.isEmpty()) {
//            message = "Introducir SUB MODELO";
//            showMessageAddVehicle(message);
//        }
        else if (year == null || year.isEmpty()) {
            message = "Introducir AÑO";
            showMessageAddVehicle(message);
        } else if (boxType == null || boxType.isEmpty()) {
            message = "Introducir TIPO DE CAJA";
            showMessageAddVehicle(message);
        } else if (transmissionType == null || transmissionType.isEmpty()) {
            message = "Introducir TIPO DE TRANSMISIÓN";
            showMessageAddVehicle(message);
        } else if (fuelType == null || fuelType.isEmpty()) {
            message = "Introducir TIPO DE COMBUSTIBLE";
            showMessageAddVehicle(message);
        } else {
            ConfirmAddVehicleDialogFragment c = ConfirmAddVehicleDialogFragment.newInstance("¿Está seguro que desea agregar el vehículo?");
            c.show(getSupportFragmentManager(), "CONFIRM_ADD_VEHICLE_DIALOG");
            c.setCancelable(false);
        }
    }

    private void showMessageAddVehicle(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void enableInputs() {
        activity_add_vehicle_vehicle_confirm_btn.setEnabled(true);
    }

    @Override
    public void disableInputs() {
        activity_add_vehicle_vehicle_confirm_btn.setEnabled(false);
    }

    @Override
    public void showProgress() {
        activity_add_vehicle_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity_add_vehicle_progress.setVisibility(View.GONE);
    }

    private ArrayAdapter<TipoVehiculo> adapterVehicleType;
    private ArrayAdapter<String> adapterBoxType;
    private ArrayAdapter<String> adapterTransmissionType;
    private ArrayAdapter<String> adapterFuelType;

    @Override
    public void providerVehiclesTypeSpinner(List<TipoVehiculo> vehiclesType) {
        adapterVehicleType = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_eliot_onpressed, vehiclesType);
//        adapterVehicleType.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_eliot);
        activity_add_vehicle_vehicle_type.setAdapter(adapterVehicleType);
    }

    @Override
    public void providerBoxType(TipoCaja boxType) {
        List<String> bt = new ArrayList<>();
        bt.add(boxType.getAutomatica());
        bt.add(boxType.getMecanica());
        adapterBoxType = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_eliot_onpressed, bt);
        activity_add_vehicle_vehicle_type_box.setAdapter(adapterBoxType);
    }

    @Override
    public void providerTrasnmissionType(List<String> transmissionType) {
        adapterTransmissionType = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_eliot_onpressed, transmissionType);
        activity_add_vehicle_vehicle_transmission.setAdapter(adapterTransmissionType);
    }

    @Override
    public void providerFuelType(List<String> fuelType) {
        adapterFuelType = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_eliot_onpressed, fuelType);
        activity_add_vehicle_vehicle_fuel_type.setAdapter(adapterFuelType);
    }

    @Override
    public void addVehicleError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MyVehiclesActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnerId = parent.getId();
        Object object = parent.getItemAtPosition(position);
        switch (spinnerId) {
            case R.id.activity_add_vehicle_vehicle_type:
                if (object instanceof TipoVehiculo) {
                    TipoVehiculo tipoVehiculo = (TipoVehiculo) object;
                    vehicleType = tipoVehiculo.getId();
                }
                break;
            case R.id.activity_add_vehicle_vehicle_type_box:
                if (object instanceof String) {
                    boxType = (String) object;
                }
                break;
            case R.id.activity_add_vehicle_vehicle_transmission:
                if (object instanceof String) {
                    transmissionType = (String) object;
                }
                break;
            case R.id.activity_add_vehicle_vehicle_fuel_type:
                if (object instanceof String) {
                    fuelType = (String) object;
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int id) {
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        mPresenter.handleAddVehicleConfirm(alias,
                licensePlate,
                vehicleType,
                brand,
                model,
                submodel,
                year,
                boxType,
                transmissionType,
                fuelType);
    }
}
