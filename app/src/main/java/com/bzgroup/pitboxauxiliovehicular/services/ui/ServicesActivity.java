package com.bzgroup.pitboxauxiliovehicular.services.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.ServiceRequestScreen.ServiceRequestActivity;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.ui.AddVehicleActivity;
import com.bzgroup.pitboxauxiliovehicular.dialogs.DeniedLocationPersmissionDialogFragment;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.order.Pedido;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.services.IServicesPresenter;
import com.bzgroup.pitboxauxiliovehicular.services.ServicesPresenter;
import com.bzgroup.pitboxauxiliovehicular.services.adapter.AdapterMyAddress;
import com.bzgroup.pitboxauxiliovehicular.services.adapter.AdapterServices;
import com.bzgroup.pitboxauxiliovehicular.services.modals.FindingSupplierBottomSheetDialog;
import com.bzgroup.pitboxauxiliovehicular.services.modals.MyVehiclesEmptyBottomSheetDialog;
import com.bzgroup.pitboxauxiliovehicular.services.modals.ScheduleBottomSheetDialog;
import com.bzgroup.pitboxauxiliovehicular.utils.IOnItemClickListener;
import com.bzgroup.pitboxauxiliovehicular.utils.Permission;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServicesActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, LocationListener, DeniedLocationPersmissionDialogFragment.DeniedLocationPersmissionDialogFragmentListener, IOnItemClickListener, AdapterView.OnItemSelectedListener, IServicesView, ScheduleBottomSheetDialog.ScheduleBottomSheetDialogListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, View.OnClickListener, MyVehiclesEmptyBottomSheetDialog.MyVehiclesEmptyBottomSheetDialogListener {

    public static final String TAG = ServicesActivity.class.getSimpleName();

    private static final int REQUEST_CHECK_SETTINGS = 911;

    private GoogleMap mMap;

    private LocationRequest mLocationRequest;
    private LocationManager mlocationManager;

    private FusedLocationProviderClient mFusedLocationClient;

    @BindView(R.id.activity_services_spinner_myvehicles)
    Spinner activity_services_spinner_myvehicles;
    @BindView(R.id.activity_services_spinner_services)
    Spinner activity_services_spinner_services;

    @BindView(R.id.activity_services_request_btn)
    Button activity_services_request_btn;
    @BindView(R.id.activity_services_progress)
    ProgressBar activity_services_progress;
    @BindView(R.id.activity_services_base_price)
    TextView activity_services_base_price;
    @BindView(R.id.activity_services_rv)
    RecyclerView recyclerView;
    AdapterServices mAdapter;
    @BindView(R.id.activity_services_container)
    ConstraintLayout activity_services_container;

    @BindView(R.id.activity_service_schedule)
    ImageView activity_service_schedule;

    // BottomSheet Set Address
    @BindView(R.id.bottom_sheet_set_location)
    ConstraintLayout bottom_sheet_set_location;
    private BottomSheetBehavior bottomSheetBehavior;
    @BindView(R.id.bs_set_location_close)
    ImageButton bs_set_location_close;
    @BindView(R.id.bs_set_location_my_location)
    ConstraintLayout bs_set_location_my_location;
    @BindView(R.id.bs_set_location_rv)
    RecyclerView bs_set_location_rv;
    AdapterMyAddress adapterMyAddress;
    @BindView(R.id.bs_set_location_emtpy_container)
    ConstraintLayout bs_set_location_emtpy_container;
    @BindView(R.id.bs_set_location_emtpy_txt)
    TextView bs_set_location_emtpy_txt;

    // Bottom Sheet Add Address
    @BindView(R.id.bottom_sheet_add_address)
    ConstraintLayout bottom_sheet_add_address;
    private BottomSheetBehavior bottomSheetBehaviorAddAddress;
    @BindView(R.id.bs_add_address_name)
    TextView bs_add_address_name;
    @BindView(R.id.bs__add_address_description)
    TextInputEditText bs__add_address_description;

    // Bottom Sheet Finding Supplier
    @BindView(R.id.bottom_sheet_finding_supplier)
    ConstraintLayout bottom_sheet_finding_supplier;
    private BottomSheetBehavior bottomSheetBehaviorFindingSupplier;

    private String categorieId;
    private boolean isSchedule;
    private IServicesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setClickable(true);
        toolbar.setOnClickListener(this);
        ButterKnife.bind(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_set_location);
        bottomSheetBehaviorAddAddress = BottomSheetBehavior.from(bottom_sheet_add_address);
        bottomSheetBehaviorFindingSupplier = BottomSheetBehavior.from(bottom_sheet_finding_supplier);
        setupSheetBehavior();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_sec);
        mapFragment.getMapAsync(this);

        categorieId = getIntent().getStringExtra("CATEGORIE_ID");
        isSchedule = getIntent().getBooleanExtra("IS_SCHEDULE", false);

        if (!Permission.validateLocationPermissions(getApplicationContext()))
            Permission.requestLocationPermissions(this);
        else
            locationStart();

        mPresenter = new ServicesPresenter(this);
        mPresenter.onCreate();
        mPresenter.handleMyVehicles();

        setupAdapter();
        setupRecycler();
        setupSpinnerListeners();
        setupLayout();
    }

    private void setupSheetBehavior() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.d(TAG, "onStateChanged: " + i);
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.d(TAG, "onSlide: ");
            }
        });

        bottomSheetBehaviorAddAddress.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.d(TAG, "onStateChanged: " + i);
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.d(TAG, "onSlide: ");
            }
        });
        bottomSheetBehaviorFindingSupplier.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                Log.d(TAG, "onStateChanged: " + i);
                switch (i) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehaviorFindingSupplier.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
                    }
                    break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.d(TAG, "onSlide: ");
            }
        });
    }

    private void setupLayout() {
        geocoder = new Geocoder(this, Locale.getDefault());
        if (isSchedule)
            activity_service_schedule.setVisibility(View.VISIBLE);
        else
            activity_service_schedule.setVisibility(View.GONE);
    }

    private void setupSpinnerListeners() {
        activity_services_spinner_myvehicles.setOnItemSelectedListener(this);
        activity_services_spinner_services.setOnItemSelectedListener(this);
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        bs_set_location_rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        bs_set_location_rv.setLayoutManager(layoutManager2);
        bs_set_location_rv.setAdapter(adapterMyAddress);

    }

    private void setupAdapter() {
        mAdapter = new AdapterServices(this, new ArrayList<>(), this);
        adapterMyAddress = new AdapterMyAddress(this, new ArrayList<>(), false, this);
    }

    @Override
    public void providerAddress(List<com.bzgroup.pitboxauxiliovehicular.entities.Address> addresses) {
        adapterMyAddress.add(addresses);
    }

    @Override
    public void showContainerEmptyMyAddress() {
        bs_set_location_emtpy_container.setVisibility(View.VISIBLE);
        bs_set_location_rv.setVisibility(View.GONE);
    }

    @Override
    public void hideContainerEmptyMyAddress() {
        bs_set_location_emtpy_container.setVisibility(View.GONE);
        bs_set_location_rv.setVisibility(View.VISIBLE);
    }

    public void locationStart() {
        if (!verificarGPS_Activo()) {
//            fragment_addresses_cv.setVisibility(View.VISIBLE);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
            createLocationRequest();
            getCurrentLocationSetting();
        }
//        else
//            fragment_addresses_cv.setVisibility(View.GONE);
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void getCurrentLocationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getApplicationContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "getCurrentLocationSetting onSuccess: ");
//                fragment_addresses_cv.setVisibility(View.GONE);
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        Log.d(TAG, "getCurrentLocationSetting onFailure: ");
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(ServicesActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public boolean verificarGPS_Activo() {
        mlocationManager = (LocationManager) Objects.requireNonNull(getApplication()).getSystemService(Context.LOCATION_SERVICE);
        return mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveStartedListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        if (currentLocation != null) {
            LatLng scz = new LatLng(currentLocation.latitude, currentLocation.longitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(scz)      // Sets the center of the map to Mountain View
                    .zoom(15)                   // Sets the orientation of the camera to east
                    // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-17.781943, -63.180489), 12));

        // Add a marker in Sydney and move the camera
//        LatLng sydney = ;
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        if (addressesFragment != null)
//                            addressesFragment.hideMessageEnabledGPS();
                        break;
                    case Activity.RESULT_CANCELED:
                        ServicesActivity.A s = new ServicesActivity.A("", getString(R.string.activity_service_stations_denied_location_settings), DeniedLocationPersmissionDialogFragment.DENIED_SETTINGS_FIRST_TIME);
                        s.execute();
                        break;
                    default:
                        break;
                }
                break;
            case REQUEST_PERMISSION_SETTING:
                Log.d(TAG, "onActivityResult: REQUEST_PERMISSION_SETTING");
                if (!Permission.validateLocationPermissions(this)) {
//                    Permission.requestLocationPermissions(this);
                    A s = new A("fsdfsdfsdfs", getString(R.string.activity_service_stations_denied_location_permission_never_ask_again), DeniedLocationPersmissionDialogFragment.DENIED_MORE_TIME);
                    s.execute();
                } else {
                    locationStart();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode " + requestCode + " permissions " + permissions.toString() + " grantResults " + grantResults.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Permission.MULTIPLE_PERMISSIONS_REQUEST_CODE:
                // Verifica si todos los permisos se aceptaron o no
                if (Permission.validatePermissions(getApplicationContext(), grantResults)) {
                    locationStart();
                    //Si todos los permisos fueron aceptados continua con el flujo normal
                } else if (Permission.validateLocationPermissions(getApplicationContext()))
                    locationStart();

                else {
                    //Si algun permiso fue rechazado no se puede continuar
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Mostrar una extensión al usuario * de forma asíncrona * - no bloquear
                        // este hilo esperando la respuesta del usuario! Despues del usuario
                        // ve la explicación, intente nuevamente para solicitar el permiso.
                        ServicesActivity.A s = new ServicesActivity.A("", getString(R.string.activity_service_stations_denied_location_permission), DeniedLocationPersmissionDialogFragment.DENIED_FIRST_TIME);
                        s.execute();
                    } else {
                        // Don't ask again check
                        ServicesActivity.A s = new ServicesActivity.A("", getString(R.string.activity_service_stations_denied_location_permission_never_ask_again), DeniedLocationPersmissionDialogFragment.DENIED_MORE_TIME);
                        s.execute();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    boolean locationB = true;
    private LatLng currentLocation;
    private LatLng currentLocationCameraIdle;

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: ");
        if (locationB) {
            if (mMap != null) {
                Log.d(TAG, "onLocationChanged: mMap != null");
//                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.setMyLocationEnabled(true);
                LatLng scz = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(scz)      // Sets the center of the map to Mountain View
                        .zoom(15)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                currentLocation = cameraPosition.target;
                currentLocationCameraIdle = currentLocation;
//            mPresenter.handleServiceStations();
            }
            locationB = false;
        }
        if (mlocationManager != null) {
            mlocationManager.removeUpdates(this);
            mlocationManager = null;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int id) {
        switch (id) {
            case DeniedLocationPersmissionDialogFragment.DENIED_FIRST_TIME:
                Permission.requestLocationPermissions(this);
                break;
            case DeniedLocationPersmissionDialogFragment.DENIED_MORE_TIME:
                openSettings();
                break;
            case DeniedLocationPersmissionDialogFragment.DENIED_SETTINGS_FIRST_TIME:
                locationStart();
                break;
        }
    }

    private static final int REQUEST_PERMISSION_SETTING = 159;

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private Service service;

    @Override
    public void onItemClick(Object object) {
//        if (object instanceof Service) {
//            service = (Service) object;
//            activity_services_base_price.setText(service.getPrecioBase());
//        }
        if (object instanceof com.bzgroup.pitboxauxiliovehicular.entities.Address) {
//            putLocation();
            com.bzgroup.pitboxauxiliovehicular.entities.Address address = (com.bzgroup.pitboxauxiliovehicular.entities.Address) object;
            LatLng latLng = new LatLng(Double.valueOf(address.getLatitude()), Double.valueOf(address.getLongitude()));
            if (latLng != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        // Sets the orientation of the camera to east
                        // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onItemSelected(Object object) {

    }

    @Override
    public void showProgress() {
        activity_services_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity_services_progress.setVisibility(View.GONE);
    }

    @Override
    public void providerMyVehicles(List<Vehicle> myVehicles) {
        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_dropdown_item_eliot, myVehicles);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_eliot_onpressed);
        activity_services_spinner_myvehicles.setAdapter(adapter);
    }

    @Override
    public void providerServices(List<Service> services) {
        ArrayAdapter<Service> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_dropdown_item_eliot, services);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_eliot_onpressed);
        activity_services_spinner_services.setAdapter(adapter);
    }

    @Override
    public void providerServicesEmpty(String message) {

    }

    @Override
    public void providerMyVehiclesEmpty(String message) {
        activity_services_container.setVisibility(View.GONE);
        MyVehiclesEmptyBottomSheetDialog myVehiclesEmptyBottomSheetDialog = MyVehiclesEmptyBottomSheetDialog.newInstance(message);
        myVehiclesEmptyBottomSheetDialog.show(getSupportFragmentManager(), EMPTY_VEHICLES_DIALOG);
//        myVehiclesEmptyBottomSheetDialog.setCancelable(false);
//        myVehiclesEmptyBottomSheetDialog.onDismiss(this);


    }

    @Override
    public void providerMyAddressIsEmpty(String errorMessage) {
//        bs_set_location_emtpy_container.setVisibility(View.VISIBLE);
        bs_set_location_emtpy_txt.setText(errorMessage);
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bs_set_location_emtpy_add_btn)
    public void handleEmptyAddAddress() {
//        Toast.makeText(this, "Agregar nueva dirección", Toast.LENGTH_SHORT).show();
        isServices = false;
        enabledServicesContainer(false);
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        if (bottomSheetBehaviorAddAddress.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void enabledInputs() {
        activity_services_request_btn.setEnabled(true);
    }

    @Override
    public void disabledInputs() {
        activity_services_request_btn.setEnabled(false);
    }

    @OnClick(R.id.activity_services_request_btn)
    public void handleServiceRequest() {
//        showProgress();
//        disabledInputs();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String[] d = currentDateandTime.split("_");
        String date = d[0];
        String time = d[1];
        if (scheduleDate != null && !scheduleDate.isEmpty() && scheduleTime != null && !scheduleTime.isEmpty()) {
            if (service != null && currentLocationCameraIdle != null && mVehicle != null) {
                Log.d(TAG, "handleServiceRequest currentLocation: " + currentLocationCameraIdle.toString());
                Log.d(TAG, "handleServiceRequest service: " + service.toString());
                Log.d(TAG, "handleServiceRequest mVehicle: " + mVehicle.toString());
                Log.d(TAG, "handleServiceRequest Date: " + scheduleDate);
                Log.d(TAG, "handleServiceRequest Time: " + scheduleTime);
                mPresenter.handleOrder(mVehicle.getId(),
                        service.getId(),
                        currentLocationCameraIdle.latitude,
                        currentLocationCameraIdle.longitude,
                        scheduleDate, scheduleTime, "");
            }
        } else {
            if (service != null && currentLocationCameraIdle != null && mVehicle != null) {
                Log.d(TAG, "handleServiceRequest currentLocation: " + currentLocationCameraIdle.toString());
                Log.d(TAG, "handleServiceRequest service: " + service.toString());
                Log.d(TAG, "handleServiceRequest mVehicle: " + mVehicle.toString());
                Log.d(TAG, "handleServiceRequest Date: " + date);
                Log.d(TAG, "handleServiceRequest Time: " + time);
                mPresenter.handleOrder(mVehicle.getId(),
                        service.getId(),
                        currentLocationCameraIdle.latitude,
                        currentLocationCameraIdle.longitude,
                        date, time, "");
            }
        }

    }

    private static String SCHEDULE_DIALOG = "SCHEDULE_DIALOG";
    private static String EMPTY_VEHICLES_DIALOG = "EMPTY_VEHICLES_DIALOG";

    @OnClick(R.id.activity_service_schedule)
    public void handleScheduleService() {
//        ScheduleBottomSheetDialog scheduleBottomSheetDialog = new ScheduleBottomSheetDialog();
        ScheduleBottomSheetDialog scheduleBottomSheetDialog = new ScheduleBottomSheetDialog();
        scheduleBottomSheetDialog.show(getSupportFragmentManager(), SCHEDULE_DIALOG);
    }

    private Vehicle mVehicle;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemSelected: ");
        int spinnerId = parent.getId();
        Object object = parent.getItemAtPosition(position);
        switch (spinnerId) {
            case R.id.activity_services_spinner_myvehicles:
                if (object instanceof Vehicle) {
                    mVehicle = (Vehicle) object;
                    mPresenter.handleServices(categorieId);
                }
                break;
            case R.id.activity_services_spinner_services:
                if (object instanceof Service) {
                    service = (Service) object;
                    activity_services_base_price.setText(service.getPrecioBase());
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d(TAG, "onNothingSelected: ");
    }

    private String currentDate;
    private String currentTime;
    private String scheduleDate;
    private String scheduleTime;

    @Override
    public void onScheduleButtonClick(String date, String time) {
        scheduleDate = date;
        scheduleTime = time;
        Toast.makeText(this, date + " " + time, Toast.LENGTH_SHORT).show();
    }

    private Geocoder geocoder;

    @OnClick(R.id.bs_add_address_btn_close)
    public void handleAddAddressClose() {
        if (bottomSheetBehaviorAddAddress.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_COLLAPSED);
        isServices = true;
        activity_services_container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCameraIdle() {
        Log.d(TAG, "onCameraIdle: ");
        activity_services_container.setVisibility(isServices ? View.VISIBLE : View.GONE);
//        sleepScreen(1000);
        LatLng latLng = mMap.getCameraPosition().target;
        currentLocationCameraIdle = latLng;
        Log.d(TAG, "onCameraIdle: latlog: " + mMap.getCameraPosition().target.toString());

        List<Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, get just a single address.
                    1);

            if (addresses == null || addresses.size() == 0) {
                Log.d(TAG, "onCameraIdle: addresses == null || addresses.size()  == 0");
            } else {
                Address address = addresses.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    String[] addres = address.getAddressLine(i).split(",");
                    if (addres.length > 0) {
//                        if (isServices)
                        getSupportActionBar().setTitle(addres[0]);
//                        else
                        bs_add_address_name.setText(addres[0]);
                    }
                }
            }

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            Log.e(TAG, "onCameraIdle ", ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            Log.e(TAG, "onCameraIdle. " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " +
                    latLng.longitude, illegalArgumentException);
        }
    }

    private void sleepScreen(int i) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity_services_container.setVisibility(View.VISIBLE);
            }
        }, i);
    }

    @Override
    public void onCameraMove() {
        if (isServices)
            getSupportActionBar().setTitle("Cargando..");
        else
            bs_add_address_name.setText("Cargando..");
        Log.d(TAG, "onCameraMove: ");
    }

    private boolean isServices = true;

    private void enabledServicesContainer(boolean enabled) {
//        if (enabled)
//        activity_services_container.setVisibility(View.VISIBLE);
//        else
//            activity_services_container.setVisibility(View.GONE);
        activity_services_container.setVisibility(View.GONE);
    }

    @OnClick(R.id.bs_add_address_btn_check)
    public void handleAddAddressCheck() {
        if (BottomSheetBehavior.STATE_EXPANDED == bottomSheetBehaviorAddAddress.getState()) {
            if (currentLocationCameraIdle != null && !bs__add_address_description.getText().toString().isEmpty()) {
//                AppPreferences appPreferences = AppPreferences.getInstance(this);
//                appPreferences.writeString(AppPreferences.Keys.USER_LAT_LOCATION, String.valueOf(mLatLng.latitude));
//                appPreferences.writeString(AppPreferences.Keys.USER_LNG_LOCATION, String.valueOf(mLatLng.longitude));
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                Toast.makeText(this, "Ubicación seleccionada correctamente.", Toast.LENGTH_LONG).show();
                isServices = true;
//                getSupportActionBar().setTitle();
//                    mCart.setDescription(activity_bs_location.getText().toString());
                bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                mPresenter.handleTransportationAmount(Integer.valueOf(mWholesaler.getId()));
                mPresenter.handleAddAddress(currentLocationCameraIdle.latitude,
                        currentLocationCameraIdle.longitude,
                        bs__add_address_description.getText().toString());
            } else if (bs__add_address_description.getText().toString().isEmpty()) {
                Toast.makeText(this, "Ingrese una descripción.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void addAddressSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        isServices = true;
        activity_services_container.setVisibility(View.VISIBLE);
//        enabledServicesContainer(true);
    }

    @Override
    public void addAddressError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContainerOrder() {
//        Toast.makeText(this, "Mostrar cargando...", Toast.LENGTH_SHORT).show();
//        FindingSupplierBottomSheetDialog b = new FindingSupplierBottomSheetDialog();
//        b.show(getSupportFragmentManager(), "FINDING_SUPPLIER_BSD");
        if (bottomSheetBehaviorFindingSupplier.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorFindingSupplier.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void providerOrderSuccess(Pedido order, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mPresenter.handleGetSupplier(order.getId());
    }

    @Override
    public void showOrderErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideServicesContainer() {
        activity_services_container.setVisibility(View.GONE);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (isServices)
            getSupportActionBar().setTitle("Cargando..");
        else
            bs_add_address_name.setText("Cargando..");
        if (isServices)
            enabledServicesContainer(true);
        else
            enabledServicesContainer(false);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Toolbar) {
            Log.d(TAG, "onClick: ");
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
            mPresenter.handleMyAddress();
        }
    }

    @OnClick(R.id.bs_set_location_my_location)
    public void handleBsSetMyLocation() {
        putLocation();
    }

    private void putLocation() {
        if (currentLocation != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    // Sets the orientation of the camera to east
                    // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.bs_set_location_close)
    public void handleBsSetLocationClose() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void addVehicleClick() {
        startActivity(new Intent(this, AddVehicleActivity.class));
    }

    @Override
    public void onDismissListener() {
        activity_services_container.setVisibility(View.GONE);
        isServices = false;
    }

    public class A extends AsyncTask<Void, Void, Void> {

        private String mTitle, mBody;
        private int mId;

        public A(String title, String body, int id) {
            mTitle = title;
            mBody = body;
            mId = id;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            showDeniedLocationDialog(mTitle, mBody, mId);
            return null;
        }
    }

    public void showDeniedLocationDialog(String title, String body, int id) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DeniedLocationPersmissionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("DENIED_LOCATION_PERMISSION", body);
        bundle.putInt("DENIED_LOCATION_PERMISSION_ID", id);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "DeniedLocationPersmissionDialogFragment");
        dialog.setCancelable(false);
    }
}
