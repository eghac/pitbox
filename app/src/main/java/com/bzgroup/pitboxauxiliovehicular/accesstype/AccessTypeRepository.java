package com.bzgroup.pitboxauxiliovehicular.accesstype;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.accesstype.event.AccessTypeEvent;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.menu.event.CategorieEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class AccessTypeRepository implements IAccessTypeRepository {

    private String GOOGLE_USER = "1";
    private String FACEBOOK_USER = "2";
    private String TAG = AccessTypeRepository.class.getSimpleName();
    private String URL_GET_USER_ID = Constants.GLOBAL_URL + "clientes/registro";
    private Context mContext;
    private AppPreferences mPreferences;

    public AccessTypeRepository(Context context) {
        mContext = context;
        mPreferences = AppPreferences.getInstance(context);
    }

    @Override
    public void handleSignInFirebase(String uid, String email, String firstName, String lastName, String urlPhoto) {
        requestUserId(GOOGLE_USER, uid, email, firstName, lastName, urlPhoto);
    }

    private void requestUserId(String typeUser, String uid, String email, String firstName, String lastName, String urlPhoto) {
        HashMap<String, String> data = new HashMap<>();
        data.put("tipo_login", typeUser);
        data.put("uid", uid);
        data.put("nombres", firstName);
        data.put("apellidos", "hola");
        data.put("email", email);
        Log.d(TAG, "requestUserId: urlPhoto: " + urlPhoto);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_GET_USER_ID, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 200:
                                    loadRequestUserId(response);
                                    break;
                                case 422:
                                    postEvent(AccessTypeEvent.ACCESS_TYPE_ERROR, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEvent(AccessTypeEvent.ACCESS_TYPE_ERROR, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "requestUserId error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(AccessTypeEvent.ACCESS_TYPE_ERROR, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestUserId(JSONObject response) throws JSONException {
        Log.d(TAG, "loadRequestUserId: " + response.toString());
        mPreferences.writeString(AppPreferences.Keys.USER_UUID, String.valueOf(response.getJSONObject("data").getInt("id")));
        postEvent(AccessTypeEvent.ACCESS_TYPE_SUCCESS, response.getString("message"));
    }

    private static void postEvent(int type, String errorMessage) {
        AccessTypeEvent accessTypeEvent = new AccessTypeEvent();
        accessTypeEvent.setEventType(type);
        if (errorMessage != null)
            accessTypeEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(accessTypeEvent);
    }
}
