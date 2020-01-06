package com.bzgroup.pitboxauxiliovehicular.menu;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.menu.event.CategorieEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.bzgroup.pitboxauxiliovehicular.utils.Constants.GLOBAL_URL;

public class CategoriesRepository implements ICategoriesRepository {

    private static final String TAG = CategoriesRepository.class.getSimpleName();
    private static final String URL_MENU = GLOBAL_URL + "categorias";
    private Context mContext;

    public CategoriesRepository(Context object) {
        mContext = (Context) object;
    }

    @Override
    public void handleCategories() {
        requestMenu();
    }

    private void requestMenu() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL_MENU, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            loadRequestCategories(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "requestCategories error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(CategorieEvent.CATEGORIES_ERROR, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestCategories(JSONArray response) throws JSONException {
        List<Categorie> categories = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject categorie = response.getJSONObject(i);
            JSONObject image = categorie.getJSONObject("icono");
            categories.add(new Categorie(
                    categorie.getString("id"),
                    categorie.getString("nombre"),
                    image.getString("url"),
                    categorie.getBoolean("programable")
            ));
        }
        if (categories.size() > 0) {
//            Log.d(TAG, "loadRequestCategories: " + categories.size());
//            mAdapter.add(categories);
            postEvent(CategorieEvent.CATEGORIES_SUCCESS, categories);
        } else {
            postEvent(CategorieEvent.CATEGORIES_EMPTY, null);
        }
    }

    private static void postEvent(int type, List<Categorie> categories) {
        postEvent(type, categories, null);
    }

    private static void postEvent(int type, List<Categorie> categories, String errorMessage) {
        CategorieEvent categorieEvent = new CategorieEvent();
        categorieEvent.setEventType(type);
        if (categories != null)
            categorieEvent.setCategories(categories);
        if (errorMessage != null)
            categorieEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(categorieEvent);
    }
}
