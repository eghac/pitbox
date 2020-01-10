package com.bzgroup.pitboxauxiliovehicular.myaddress;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.myaddress.events.MyAddressEvent;
import com.bzgroup.pitboxauxiliovehicular.myaddress.ui.MyAddressActivyt;
import com.bzgroup.pitboxauxiliovehicular.services.ServicesRepository;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;
import com.bzgroup.pitboxauxiliovehicular.vehicles.events.MyVehiclesEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bzgroup.pitboxauxiliovehicular.utils.Constants.GLOBAL_URL;

public class MyAddressRepository implements IMyAddressRepository {

    private static final String TAG = MyAddressRepository.class.getSimpleName();
    private static final String URL_MYADDRESS = GLOBAL_URL + "clientes/";
    private static final String URL_ADD_ADDRESS = GLOBAL_URL + "direcciones";

    private Context mContext;

    public MyAddressRepository(Context context) {
        mContext = context;
    }

    @Override
    public void handleMyAddress() {
        requestMyAddress();
    }

    @Override
    public void handleAddAddress(double latitude, double longitude, String description) {
        requestAddAddress(latitude, longitude, description);
    }

    private void requestAddAddress(double latitude, double longitude, String description) {
        String userUuid = getUserUuid();
        if (userUuid == null)
            return;
        HashMap<String, String> data = new HashMap<>();
        data.put("latitud", String.valueOf(latitude));
        data.put("longitud", String.valueOf(longitude));
        data.put("descripcion", description);
        data.put("cliente_id", userUuid);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_ADD_ADDRESS, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 201:
                                    loadRequestAddAddress(response);
                                    break;
                                case 422:
                                    postEvent(MyAddressEvent.MY_ADD_ADDRESS_ERROR, null, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEvent(MyAddressEvent.MY_ADD_ADDRESS_ERROR, null, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEvent(MyAddressEvent.MY_ADD_ADDRESS_ERROR, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestAddAddress(JSONObject response) throws JSONException {
        Log.d(TAG, "loadRequestAddAddress: " + response.toString());
        postEvent(MyAddressEvent.MY_ADD_ADDRESS_SUCCESS, null, response.getString("message"));
    }

    private void requestMyAddress() {
        String userUuid = getUserUuid();
        if (userUuid == null)
            return;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_MYADDRESS + userUuid + "/direcciones", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 200:
                                    loadRequestMyAddress(response);
                                    break;
                                case 422:
                                    postEvent(MyAddressEvent.MY_ADDRESS_EMPTY, null, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEvent(MyAddressEvent.MY_ADDRESS_ERROR, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestMyAddress(JSONObject response) throws JSONException {
        JSONArray array = response.getJSONArray("data");
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            addresses.add(new Address(
                    jsonObject.getString("id"),
                    jsonObject.getString("latitud"),
                    jsonObject.getString("longitud"),
                    jsonObject.getString("descripcion")
            ));
        }
        if (!addresses.isEmpty())
            postEvent(MyAddressEvent.MY_ADDRESS_SUCCESS, addresses);
        else
            postEvent(MyAddressEvent.MY_ADDRESS_EMPTY, null, response.getString("message"));
    }

    private String getUserUuid() {
        String userUuid = AppPreferences.getInstance(mContext).readString(AppPreferences.Keys.USER_UUID);
        if (userUuid == null || userUuid.isEmpty()) {
            postEvent(MyAddressEvent.MY_ADDRESS_ERROR, null, "Hubo un error al obtener mis ubicaciones.");
            return null;
        }
        return userUuid;
    }

    private void postEvent(int type, List<Address> addresses) {
        postEvent(type, addresses, null);
    }

    private static void postEvent(int type, List<Address> addresses, String errorMessage) {
        MyAddressEvent myAddressEvent = new MyAddressEvent();
        myAddressEvent.setEventType(type);
        if (addresses != null)
            myAddressEvent.setAddresses(addresses);
        if (errorMessage != null)
            myAddressEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(myAddressEvent);
    }
}
