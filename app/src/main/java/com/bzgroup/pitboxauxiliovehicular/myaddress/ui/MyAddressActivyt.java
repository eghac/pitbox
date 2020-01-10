package com.bzgroup.pitboxauxiliovehicular.myaddress.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bzgroup.pitboxauxiliovehicular.dialogs.DeniedLocationPersmissionDialogFragment;
import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.myaddress.IMyAddressPresenter;
import com.bzgroup.pitboxauxiliovehicular.myaddress.MyAddressPresenter;
import com.bzgroup.pitboxauxiliovehicular.services.adapter.AdapterMyAddress;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAddressActivyt extends AppCompatActivity implements LocationListener, OnMapReadyCallback, IMyAddressView, IOnItemClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {

    private static String TAG = MyAddressActivyt.class.getSimpleName();
    private static final int REQUEST_CHECK_SETTINGS = 911;

    @BindView(R.id.activity_my_address_progress)
    ProgressBar activity_my_address_progress;
    private IMyAddressPresenter mPresenter;
    @BindView(R.id.activity_my_address_rv)
    RecyclerView activity_my_address_rv;
    private AdapterMyAddress mAdapter;
    @BindView(R.id.activity_my_address_add_btn)
    Button activity_my_address_add_btn;

    // Bottom Sheet Add Address
    @BindView(R.id.bs_add_address_name)
    TextView bs_add_address_name;
    @BindView(R.id.bs__add_address_description)
    TextInputEditText bs__add_address_description;
    @BindView(R.id.bottom_sheet_add_address)
    ConstraintLayout bottom_sheet_add_address;
    private BottomSheetBehavior bottomSheetBehaviorAddAddress;
    @BindView(R.id.bottom_sheet_services_add_address_map)
    ConstraintLayout bottom_sheet_services_add_address_map;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationManager mlocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address_activyt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ButterKnife.bind(this);

        bottomSheetBehaviorAddAddress = BottomSheetBehavior.from(bottom_sheet_add_address);
        setupSheetBehavior();

        setupAdapter();
        setupRecycler();
        handleAnimation();
        mPresenter = new MyAddressPresenter(this);
        mPresenter.onCreate();
        mPresenter.handleMyAddress();

        if (!Permission.validateLocationPermissions(getApplicationContext()))
            Permission.requestLocationPermissions(this);
        else
            locationStart();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_sec2);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @OnClick(R.id.activity_my_address_add_btn)
    public void handleMyAddressAdd() {
        if (bottomSheetBehaviorAddAddress.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_EXPANDED);
            bottom_sheet_services_add_address_map.setVisibility(View.VISIBLE);
        }
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
                        resolvable.startResolutionForResult(MyAddressActivyt.this, REQUEST_CHECK_SETTINGS);
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

    private static final int REQUEST_PERMISSION_SETTING = 159;

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
                        MyAddressActivyt.A s = new MyAddressActivyt.A("", getString(R.string.activity_service_stations_denied_location_settings), DeniedLocationPersmissionDialogFragment.DENIED_SETTINGS_FIRST_TIME);
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
                    MyAddressActivyt.A s = new MyAddressActivyt.A("fsdfsdfsdfs", getString(R.string.activity_service_stations_denied_location_permission_never_ask_again), DeniedLocationPersmissionDialogFragment.DENIED_MORE_TIME);
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
                        MyAddressActivyt.A s = new MyAddressActivyt.A("", getString(R.string.activity_service_stations_denied_location_permission), DeniedLocationPersmissionDialogFragment.DENIED_FIRST_TIME);
                        s.execute();
                    } else {
                        // Don't ask again check
                        MyAddressActivyt.A s = new MyAddressActivyt.A("", getString(R.string.activity_service_stations_denied_location_permission_never_ask_again), DeniedLocationPersmissionDialogFragment.DENIED_MORE_TIME);
                        s.execute();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void addAddressSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mPresenter.handleMyAddress();
    }

    @Override
    public void showAddAddressError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessageAddressEmpty(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setupSheetBehavior() {
        bottomSheetBehaviorAddAddress.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
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
            }
        });
    }

    private void setupRecycler() {
        activity_my_address_rv.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        activity_my_address_rv.setLayoutManager(layoutManager2);
        activity_my_address_rv.setAdapter(mAdapter);

    }

    private void setupAdapter() {
        geocoder = new Geocoder(this, Locale.getDefault());
        mAdapter = new AdapterMyAddress(this, new ArrayList<>(), true, this);
    }

    @OnClick(R.id.bs_add_address_btn_close)
    public void handleAddAddressClose() {
        if (bottomSheetBehaviorAddAddress.getState() == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehaviorAddAddress.setState(BottomSheetBehavior.STATE_COLLAPSED);
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
    public void showProgress() {
        activity_my_address_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activity_my_address_progress.setVisibility(View.GONE);
    }

    @Override
    public void providerMyAddress(List<Address> addresses) {
        mAdapter.add(addresses);
    }

    @Override
    public void onItemClick(Object object) {
        if (object instanceof Intent) {
            Intent intent = (Intent) object;
            Address address = (Address) intent.getSerializableExtra("MY_ADDRESS_ITEM");
            switch (intent.getIntExtra("MY_ADDRESS_TYPE", -1)) {
                case 1:
                    Toast.makeText(this, "EDITAR" + address.getDescription(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "ELIMINAR" + address.getDescription(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(Object object) {

    }

    private void handleAnimation() {
        activity_my_address_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && activity_my_address_add_btn.isShown()) {
                    Animation shake = AnimationUtils.loadAnimation(MyAddressActivyt.this, R.anim.fade_out_button);
                    activity_my_address_add_btn.startAnimation(shake);
                    activity_my_address_add_btn.setVisibility(View.GONE);
                } else if (dy < 0 && !activity_my_address_add_btn.isShown()) {
                    Animation shake = AnimationUtils.loadAnimation(MyAddressActivyt.this, R.anim.fade_in_button);
                    activity_my_address_add_btn.startAnimation(shake);
                    activity_my_address_add_btn.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    private LatLng currentLocation;

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
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-17.782742, -63.181347), 5));
    }

    private Geocoder geocoder;
    LatLng currentLocationCameraIdle;

    @Override
    public void onCameraIdle() {
//        activity_services_container.setVisibility(isServices ? View.VISIBLE : View.GONE);
//        sleepScreen(1000);
        LatLng latLng = mMap.getCameraPosition().target;
        currentLocationCameraIdle = latLng;
        Log.d(TAG, "onCameraIdle: latlog: " + mMap.getCameraPosition().target.toString());

        List<android.location.Address> addresses = new ArrayList<>();

        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    // In this sample, get just a single address.
                    1);

            if (addresses == null || addresses.size() == 0) {
                Log.d(TAG, "onCameraIdle: addresses == null || addresses.size()  == 0");
            } else {
                android.location.Address address = addresses.get(0);
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

    @Override
    public void onCameraMoveStarted(int i) {
        bs_add_address_name.setText("Cargando..");
    }

    boolean locationB = true;

    @Override
    public void onLocationChanged(Location location) {
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
