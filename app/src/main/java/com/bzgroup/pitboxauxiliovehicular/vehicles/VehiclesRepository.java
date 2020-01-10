package com.bzgroup.pitboxauxiliovehicular.vehicles;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;
import com.bzgroup.pitboxauxiliovehicular.vehicles.events.MyVehiclesEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VehiclesRepository implements IVehiclesRepository {

    private static final String TAG = VehiclesRepository.class.getSimpleName();
    private static final String URL_MY_VEHICLES = Constants.GLOBAL_URL + "clientes/";

    private Context mContext;
    private AppPreferences mPreferences;

    public VehiclesRepository(Context context) {
        mContext = context;
        mPreferences = AppPreferences.getInstance(context);
    }

    private String getUserUuid() {
        String userUuid = mPreferences.readString(AppPreferences.Keys.USER_UUID);
        if (userUuid == null || userUuid.isEmpty()) {
//            postEvent(ServicesEvent.SERVICES_MYVEHICLES_ERROR, null, null, null, "Hubo un error al obtener mis vehículos.");
            return null;
        }
        return userUuid;
    }

    @Override
    public void handleMyVehicles() {
        requestMyVehicles();
    }

    private void requestMyVehicles() {
        String userUuid = getUserUuid();
        if (userUuid == null)
            return;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_MY_VEHICLES + userUuid + "/vehiculos",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("status")) {
                        case 200:
                            loadMyVehiclesResponse(response);
                            break;
                        case 422:
                            postEvent(MyVehiclesEvent.myVehiclesIsEmpty, null, response.getString("message"));
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    postEvent(MyVehiclesEvent.showMyVehiclesError, null, e.getMessage());
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                postEvent(MyVehiclesEvent.showMyVehiclesError, null, error.getMessage());
            }
        });

        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadMyVehiclesResponse(JSONObject response) throws JSONException {
        JSONArray array = response.getJSONArray("data");
        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            vehicles.add(new Vehicle(
                    object.getString("id"),
                    object.getString("alias"),
                    object.getString("placa"),
                    object.getString("marca"),
                    object.getString("modelo"),
                    object.getString("submodelo"),
                    object.getString("tipo_caja"),
                    object.getString("transmision"),
                    object.getString("combustible"),
                    object.getString("tipo_vehiculo_id"),
                    object.getString("created_at"),
                    object.getString("cliente_id")
            ));
        }
        if (!vehicles.isEmpty())
            postEvent(MyVehiclesEvent.showMyVehiclesSuccess, vehicles, null);
        else
            postEvent(MyVehiclesEvent.myVehiclesIsEmpty, null, null);
    }

    private void postEvent(int eventType, List<Vehicle> myVehicles) {
        postEvent(eventType, myVehicles, null);
    }

    private static void postEvent(int type, List<Vehicle> myVehicles, String errorMessage) {
        MyVehiclesEvent myVehiclesEvent = new MyVehiclesEvent();
        myVehiclesEvent.setEventType(type);
        if (myVehicles != null)
            myVehiclesEvent.setVehicles(myVehicles);
        if (errorMessage != null) {
            myVehiclesEvent.setErrorMesage(errorMessage);
        }
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(myVehiclesEvent);
    }
}
