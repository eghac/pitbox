package com.bzgroup.pitboxauxiliovehicular.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.menu.event.CategorieEvent;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bzgroup.pitboxauxiliovehicular.utils.Constants.GLOBAL_URL;

public class ServicesRepository implements IServicesRepository {

    private static final String TAG = ServicesRepository.class.getSimpleName();
    private static final String URL_MYVEHICLE = GLOBAL_URL + "clientes/";
    private static final String URL_SERVICES = GLOBAL_URL + "categorias/";
    private Context mContext;

    public ServicesRepository(Context context) {
        mContext = context;
    }

    @Override
    public void handleMyVehicles() {
        requestMyVehicles();
    }

    private void requestMyVehicles() {
        String userUuid = AppPreferences.getInstance(mContext).readString(AppPreferences.Keys.USER_UUID);
        userUuid = "2";
        if (userUuid == null || userUuid.isEmpty()) {
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_ERROR, null, null, "Hubo un error al obtener mis vehículos.");
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_MYVEHICLE + userUuid + "/vehiculos", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            if (response.getInt("status") == 200)
                            loadRequestMyVehicles(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "requestMyVehicles error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(ServicesEvent.SERVICES_MYVEHICLES_ERROR, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestMyVehicles(JSONObject response) throws JSONException {
        JSONArray array = response.getJSONArray("data");
        List<Vehicle> myVehicles = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject myVehicle = array.getJSONObject(i);
            myVehicles.add(new Vehicle(
                    myVehicle.getString("id"),
                    myVehicle.getString("alias"),
                    myVehicle.getString("placa"),
                    myVehicle.getString("marca"),
                    myVehicle.getString("modelo"),
                    myVehicle.getString("submodelo"),
                    myVehicle.getString("anho"),
                    myVehicle.getString("tipo_caja"),
                    myVehicle.getString("transmision"),
                    myVehicle.getString("combustible"),
                    myVehicle.getString("created_at"),
                    myVehicle.getString("tipo_vehiculo_id")
            ));
        }
        if (!myVehicles.isEmpty())
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_SUCCESS, myVehicles, null, null);
        else
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_EMPTY, null, null, "No tenés vehículos registrados.");
    }

    @Override
    public void handleServices(String categorieId) {
        requestServices(categorieId);
    }

    private void requestServices(String categorieId) {
        Log.d(TAG, "requestServices: categorieId " + categorieId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_SERVICES + categorieId + "/servicios", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 200)
                                loadRequestServices(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "requestServices error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(ServicesEvent.SERVICES_ERROR, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestServices(JSONObject response) throws JSONException {
        Log.d(TAG, "loadRequestServices: ");
        JSONArray array = response.getJSONArray("data");
        List<Service> services = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject service = array.getJSONObject(i);
            JSONObject image = service.getJSONObject("icono");
            services.add(new Service(
                    service.getString("id"),
                    service.getString("nombre"),
                    service.getString("descripcion"),
                    service.getString("precio_base"),
                    service.getString("incremento_horario"),
                    image.getString("url")
            ));
        }
        if (!services.isEmpty())
            postEvent(ServicesEvent.SERVICES_SUCCESS, null, services, null);
        else
            postEvent(ServicesEvent.SERVICES_EMPTY, null, null, "No tenés vehículos registrados.");
    }


    private static void postEventMyVehicles(int type, List<Vehicle> myVehicles) {
        postEvent(type, myVehicles, null, null);
    }

    private static void postEventServices(int type, List<Service> services) {
        postEvent(type, null, services, null);
    }

    private static void postEvent(int type, List<Vehicle> myVehicles, List<Service> services, String errorMessage) {
        ServicesEvent servicesEventEvent = new ServicesEvent();
        servicesEventEvent.setEventType(type);
        if (myVehicles != null)
            servicesEventEvent.setMyVehicles(myVehicles);
        if (services != null)
            servicesEventEvent.setServices(services);
        if (errorMessage != null)
            servicesEventEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(servicesEventEvent);
    }
}
