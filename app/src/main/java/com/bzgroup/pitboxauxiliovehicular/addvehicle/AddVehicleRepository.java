package com.bzgroup.pitboxauxiliovehicular.addvehicle;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bzgroup.pitboxauxiliovehicular.addvehicle.events.AddVehicleEvent;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoCaja;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.TipoVehiculo;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddVehicleRepository implements IAddVehicleRepository {

    private static final String TAG = AddVehicleRepository.class.getSimpleName();
    private static final String VEHICLES_TYPE_URL = Constants.GLOBAL_URL + "tipo-vehiculos/";
    private static final String VEHICLES_BOX_TYPE_URL = Constants.GLOBAL_URL + "tipos-caja";
    private static final String VEHICLES_TRANSMISSION_TYPE_URL = Constants.GLOBAL_URL + "tipos-transmision";
    private static final String VEHICLES_FUEL_TYPE_URL = Constants.GLOBAL_URL + "tipos-combustible";
    private static final String VEHICLES_ADD_CONFIRM_URL = Constants.GLOBAL_URL + "vehiculos";

    public static final String ADD_VEHICLE_SERVER_ID = "id";
    public static final String ADD_VEHICLE_BRAND_ID = "marca";
    public static final String ADD_VEHICLE_TYPE_ID = "tipo";
    public static final String ADD_VEHICLE_TYPE_NAME = "tipo_vehiculo_nombre";
    public static final String ADD_VEHICLE_MODEL_ID = "modelo";
    public static final String ADD_VEHICLE_COLOR_ID = "color";
    public static final String ADD_VEHICLE_COLOR_NAME = "color_nombre";
    public static final String ADD_VEHICLE_YEAR_ID = "anio";
    public static final String ADD_VEHICLE_PHOTO = "fotografia";
    public static final String ADD_VEHICLE_PLAIN = "placa";
    public static final String ADD_VEHICLE_DESCRIPTION = "vehiculo_principal";
    public static final String ADD_VEHICLE_VIN = "VIN";
    public static final String ADD_VEHICLE_CUSTOMER_ID = "cliente_id";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private AppPreferences mPreferences;

    public AddVehicleRepository(Object context) {
        mContext = (Context) context;
//        mSharedPreferences = this.mContext.getSharedPreferences(ADD_NEW_VEHICLE, Context.MODE_PRIVATE);
        mPreferences = AppPreferences.getInstance(mContext);
    }
//
//    @Override
//    public void handleOnSpinnerBrands() {
//        serviceGetBrands();
//    }
//
//    @Override
//    public void handleOnSpinnerVehiclesType(int brandId) {
//        serviceGetVehiclesType(brandId);
//    }
//
//    @Override
//    public void handleOnSpinnerModels(int vehicleTypeId) {
//        serviceGetModels(vehicleTypeId);
//    }
//
//    @Override
//    public void handleOnSpinnerColors(int modelId) {
//        serviceGetColors(modelId);
//    }
//
//    @Override
//    public void handleOnSpinnerYears(int modelId, int colorId) {
//        serviceGetYears(modelId, colorId);
//    }
//
//    @Override
//    public void handleOnSpinnerSetYears(int modelId, int yearId) {
//        setPreferenceData(ADD_VEHICLE_YEAR_ID, yearId);
//    }
//
//    @Override
//    public void handleAddVehicleConfirm(String plain, String vin, String description, Bitmap mBitmap) {
//        servicePostNewVehicleData(plain, vin, description, mBitmap);
//    }
//
//    @Override
//    public void handleAddVehicleEditConfirm(int idVehiculoServer, String plain, String vin, String description, Bitmap mBitmap) {
//        serviceEditVehicleData(idVehiculoServer, plain, vin, description, mBitmap);
//    }

    @Override
    public void handleVehiclesType() {
        requestVehiclesType();
    }

    @Override
    public void handleBoxType() {
        requestBoxType();
    }

    @Override
    public void handleFuelType() {
        requestFuelType();
    }

    @Override
    public void handleAddVehicleConfirm(String alias, String licensePlate, int vehicleType, String brand, String model, String submodel, String year, String boxType, String transmissionType, String fuelType) {
        requestAddVehicleConfirm(alias, licensePlate, vehicleType, brand, model, submodel, year, boxType, transmissionType, fuelType);
    }

    private void requestAddVehicleConfirm(String alias, String licensePlate, int vehicleType, String brand, String model, String submodel, String year, String boxType, String transmissionType, String fuelType) {
        Log.d(TAG, "requestAddVehicleConfirm: licensePlate: " + licensePlate);
        String userId = getUserUuid();
        if (userId == null)
            return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("alias", alias);
        data.put("placa", licensePlate);
        data.put("marca", brand);
        data.put("modelo", model);
        data.put("submodelo", submodel);
        data.put("anho", year);
        data.put("tipo_caja", boxType);
        data.put("transmision", transmissionType);
        data.put("combustible", fuelType);
        data.put("tipo_vehiculo_id", vehicleType);
        data.put("cliente_id", userId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, VEHICLES_ADD_CONFIRM_URL, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 201)
                                loadRequestAddVehicleConfirm(response);
                            else if (response.getInt("status") == 422)
                                postEvent(AddVehicleEvent.ADD_VEHICLE_ERROR, null, null, null, null, response.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEvent(AddVehicleEvent.ADD_VEHICLE_ERROR, null, null, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    private void loadRequestAddVehicleConfirm(JSONObject response) throws JSONException {
        postEvent(AddVehicleEvent.ADD_VEHICLE_SUCESS, null, null, null, null, response.getString("message"));
    }

    private String getUserUuid() {
        String userUuid = AppPreferences.getInstance(mContext).readString(AppPreferences.Keys.USER_UUID);
        if (userUuid == null || userUuid.isEmpty()) {
            postEvent(AddVehicleEvent.ADD_VEHICLE_ERROR, null, "Hubo un error al agregar el vehículo.");
            return null;
        }
        return userUuid;
    }

    private void requestFuelType() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, VEHICLES_FUEL_TYPE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("status")) {
                        case 200:
                            loadFuelTypeResponse(response);
                            break;
                        case 402:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadFuelTypeResponse(JSONObject response) throws JSONException {
        List<String> data = new ArrayList<>();
        data.add(response.getJSONObject("data").getString("gasolina"));
        data.add(response.getJSONObject("data").getString("diesel"));
        data.add(response.getJSONObject("data").getString("gas"));
        data.add(response.getJSONObject("data").getString("electricidad"));
        data.add(response.getJSONObject("data").getString("mixto"));
        postEvent(AddVehicleEvent.ADD_VEHICLE_FUEL_TYPE_SUCCESS, null, null, null, data, null);
    }

    @Override
    public void handleTransmissionType() {
        requestTransmissionType();
    }

    private void requestTransmissionType() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, VEHICLES_TRANSMISSION_TYPE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("status")) {
                        case 200:
                            loadTransmissionTypeResponse(response);
                            break;
                        case 402:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadTransmissionTypeResponse(JSONObject response) throws JSONException {
        List<String> data = new ArrayList<>();
        data.add(response.getJSONObject("data").getString("4x2"));
        data.add(response.getJSONObject("data").getString("4x4"));
        postEvent(AddVehicleEvent.ADD_VEHICLE_TRANSMISSION_TYPE_SUCCESS, null, null, data, null, null);
    }

    private void requestBoxType() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, VEHICLES_BOX_TYPE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("status")) {
                        case 200:
                            loadBoxTypeResponse(response);
                            break;
                        case 402:
                            break;
                        case 422:
//                            No se pudo registrar el vehiculos: El campo placa ya existe en la base de datos
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadBoxTypeResponse(JSONObject response) throws JSONException {
        TipoCaja tipoCaja = new TipoCaja(
                response.getJSONObject("data").getString("automatica"),
                response.getJSONObject("data").getString("mecanica"));
        postEvent(AddVehicleEvent.ADD_VEHICLE_BOX_TYPE_SUCCESS, tipoCaja);
    }


    //    private void serviceEditVehicleData(int idVehiculoServer, String plain, String vin, String description, Bitmap mBitmap) {
//        HashMap<String, String> data = new HashMap<>();
//        data.put(ADD_VEHICLE_PLAIN, plain);
//        data.put(ADD_VEHICLE_VIN, vin);
//        data.put(ADD_VEHICLE_DESCRIPTION, description);
//        data.put(ADD_VEHICLE_BRAND_ID, mSharedPreferences.getString(ADD_VEHICLE_BRAND_ID,""));
//        data.put(ADD_VEHICLE_TYPE_ID, mSharedPreferences.getString(ADD_VEHICLE_TYPE_ID,""));
//        data.put(ADD_VEHICLE_MODEL_ID, mSharedPreferences.getString(ADD_VEHICLE_MODEL_ID,""));
//        data.put(ADD_VEHICLE_COLOR_ID, mSharedPreferences.getString(ADD_VEHICLE_COLOR_ID,""));
//        data.put(ADD_VEHICLE_YEAR_ID, mSharedPreferences.getString(ADD_VEHICLE_YEAR_ID,""));
//        SharedPreferences sp = mContext.getSharedPreferences(AccessTypeEvent.ACCESS_TYPE_SESSION, Context.MODE_PRIVATE);
//        data.put(ADD_VEHICLE_CUSTOMER_ID, sp.getString(AccessTypeEvent.CUSTOMER_ID_SERVER, ""));
//
//        String photoBase64 = "";
//        if (mBitmap != null) {
//            mBitmap = scaleDown(mBitmap, 600, true);
//            photoBase64 = "data:image/png;base64," + bitmapToBase64(mBitmap);
//            data.put(ADD_VEHICLE_PHOTO, photoBase64);
//        } else
//            data.put(ADD_VEHICLE_PHOTO, "");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.PUT, EDIT_VEHICLE_URL + idVehiculoServer, new JSONObject(data)
//                , new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    int status = response.getInt("status");
//                    switch (status) {
//                        case 200:
//                            getResponseEditVehicle(response);
//                            break;
//                        case 422:
//                            postEvent(AddVehicleEvent.editVehicleError, response.getString("message"));
//                            break;
//                    }
//
//                } catch (JSONException e) {
//                    postEvent(AddVehicleEvent.editVehicleError, e.getMessage());
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                postEvent(AddVehicleEvent.editVehicleError, error.getMessage());
//            }
//        });
//        Singleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void getResponseEditVehicle(JSONObject response) throws JSONException {
//        postEvent(AddVehicleEvent.editVehicleSuccess);
//    }
//
//    private void servicePostNewVehicleData(String plain, String vin, String description, Bitmap mBitmap) {
//        HashMap<String, String> data = new HashMap<>();
//        data.put(ADD_VEHICLE_PLAIN, plain);
//        data.put(ADD_VEHICLE_VIN, vin);
//        data.put(ADD_VEHICLE_DESCRIPTION, description);
//        data.put(ADD_VEHICLE_BRAND_ID, mSharedPreferences.getString(ADD_VEHICLE_BRAND_ID,""));
//        data.put(ADD_VEHICLE_TYPE_ID, mSharedPreferences.getString(ADD_VEHICLE_TYPE_ID,""));
//        data.put(ADD_VEHICLE_MODEL_ID, mSharedPreferences.getString(ADD_VEHICLE_MODEL_ID,""));
//        data.put(ADD_VEHICLE_COLOR_ID, mSharedPreferences.getString(ADD_VEHICLE_COLOR_ID,""));
//        data.put(ADD_VEHICLE_YEAR_ID, mSharedPreferences.getString(ADD_VEHICLE_YEAR_ID,""));
//        SharedPreferences sp = mContext.getSharedPreferences(AccessTypeEvent.ACCESS_TYPE_SESSION, Context.MODE_PRIVATE);
//        data.put(ADD_VEHICLE_CUSTOMER_ID, sp.getString(AccessTypeEvent.CUSTOMER_ID_SERVER, ""));
//
//        String photoBase64 = "";
//        if (mBitmap != null) {
//            mBitmap = scaleDown(mBitmap, 600, true);
//            photoBase64 = "data:image/png;base64," + bitmapToBase64(mBitmap);
//            data.put(ADD_VEHICLE_PHOTO, photoBase64);
//        } else
//            data.put(ADD_VEHICLE_PHOTO, "");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST, NEW_VEHICLE_URL, new JSONObject(data)
//                , new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    int status = response.getInt("status");
//                    switch (status) {
//                        case 201:
//                            getResponseAddVehicle(response);
//                            break;
//                        case 402:
////                            postEvent(AddVehicleEvent.addVehicleError, response.getString("message")));
//                            break;
//                        case 422:
//                            postEvent(AddVehicleEvent.addVehicleError, response.getString("message"));
//                            break;
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        Singleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private void getResponseAddVehicle(JSONObject response) throws JSONException {
//        postEvent(AddVehicleEvent.addVehicleSuccess);
//    }
//
//    private void serviceGetBrands() {
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, BRANDS_URL,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    switch (response.getInt("status")) {
//                        case 200:
//                            getResponseBrands(response);
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                }
//        });
//        newRequestQueue(jsonObjectRequest);
//    }
//
    private void requestVehiclesType() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, VEHICLES_TYPE_URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getInt("status")) {
                        case 200:
//                            setPreferenceData(ADD_VEHICLE_BRAND_ID, brandId);
                            getResponseVehiclesType(response);
                            break;
                        case 402:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }
//
//    private void serviceGetModels(final int vehicleTypeId) {
//        String url = MODELS_URL + "" + vehicleTypeId;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    switch (response.getInt("status")) {
//                        case 200:
//                            setPreferenceData(ADD_VEHICLE_TYPE_ID, vehicleTypeId);
//                            getResponseModels(response);
//                            break;
//                        case 402:
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        newRequestQueue(jsonObjectRequest);
//    }
//
//    private void serviceGetColors(final int modelId) {
//        String url = COLORS_URL + "" + modelId;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    switch (response.getInt("status")) {
//                        case 200:
//                            setPreferenceData(ADD_VEHICLE_MODEL_ID, modelId);
//                            getResponseColors(response);
//                            break;
//                        case 402:
//                            break;
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        newRequestQueue(jsonObjectRequest);
//    }
//
//    private void serviceGetYears(final int modelId, final int colorId) {
//        String url = YEARS_URL + "" + modelId;
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.GET, url,
//                null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    switch (response.getInt("status")) {
//                        case 200:
//                            setPreferenceData(ADD_VEHICLE_COLOR_ID, colorId);
//                            getResponseYears(response);
//                            break;
//                        case 402:
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Singleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
//    }
//
//    private boolean setPreferenceData(String key, Object value) {
//        mEditor = mSharedPreferences.edit();
//        if (value instanceof Integer) {
//            int idInt = (int) value;
//            mEditor.putString(key, String.valueOf(idInt));
//        } else if (value instanceof String) {
//            String idString = (String) value;
//            mEditor.putString(key, idString);
//        }
//        return mEditor.commit();
//    }
//
//    private void getResponseBrands(JSONObject response) throws JSONException {
//        JSONArray jsonArray = response.getJSONArray("data");
//        List<Marca> marcas = new ArrayList<>();
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject brand = jsonArray.getJSONObject(i);
//            marcas.add(new Marca(Integer.valueOf(brand.getString("id")), brand.getString("nombre"),
//                    brand.getString("fotografia"), brand.getString("activo").charAt(0),
//                    brand.getString("created_at"), brand.getString("created_at")));
//        }
//        if (marcas.size() > 0)
//            postEventBrands(AddVehicleEvent.getBrandsSuccess, marcas);
//        else
//            postEvent(AddVehicleEvent.brandsIsEmpty, "Lista de marcas vacía.");
//    }

    private void getResponseVehiclesType(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("data");
        List<TipoVehiculo> vehiclesType = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject vehicleType = jsonArray.getJSONObject(i);
            vehiclesType.add(new TipoVehiculo(
                    Integer.valueOf(vehicleType.getString("id")),
                    vehicleType.getString("nombre")));
        }
        if (vehiclesType.size() > 0)
            postEvent(AddVehicleEvent.getVehiclesTypeSuccess, vehiclesType, null);
        else
            postEvent(AddVehicleEvent.vehiclesTypeIsEmpty, null, response.getString("message"));
    }

//    private void getResponseModels(JSONObject response) throws JSONException {
//        JSONArray jsonArray = response.getJSONArray("data");
//        List<Modelo> models = new ArrayList<>();
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject model = jsonArray.getJSONObject(i);
//            models.add(new Modelo(Integer.valueOf(model.getString("id")), model.getString("nombre"),
//                    model.getString("activo").charAt(0),
//                    model.getString("created_at"), model.getString("created_at")));
//        }
//        if (models.size() > 0)
//            postEventModels(AddVehicleEvent.getModelsSuccess, models);
//        else
//            postEvent(AddVehicleEvent.modelsIsEmpty);
//    }
//
//    private void getResponseColors(JSONObject response) throws JSONException {
//        JSONArray jsonArray = response.getJSONArray("data");
//        List<Color> colors = new ArrayList<>();
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject color = jsonArray.getJSONObject(i);
//            colors.add(new Color(Integer.valueOf(color.getString("id")), color.getString("color"),
//                    color.getString("activo").charAt(0), Integer.valueOf(color.getString("modelo_id")),
//                    color.getString("created_at"), color.getString("created_at")));
//        }
//        if (colors.size() > 0)
//            postEventColors(AddVehicleEvent.getColorsSuccess, colors);
//        else
//            postEvent(AddVehicleEvent.colorsIsEmpty, "Lista de colores vacía");
//    }
//
//    private void getResponseYears(JSONObject response) throws JSONException {
//        JSONArray jsonArray = response.getJSONArray("data");
//        List<Anio> years = new ArrayList<>();
//        for (int i=0; i < jsonArray.length(); i++) {
//            JSONObject year = jsonArray.getJSONObject(i);
//            years.add(new Anio(Integer.valueOf(year.getString("id")), Integer.valueOf(year.getString("anio")),
//                    Integer.valueOf(year.getString("modelo_id")), year.getString("activo").charAt(0),
//                    year.getString("created_at"), year.getString("created_at")));
//        }
//        if (years.size() > 0 )
//            postEventYears(AddVehicleEvent.getYearsSuccess, years);
//        else
//            postEvent(AddVehicleEvent.yearsIsEmpty, "Lista de años vacía.");
//    }
//
//    private void newRequestQueue(JsonObjectRequest jsonObjectRequest) {
//        RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
//        requestQueue.add(jsonObjectRequest);
//    }

//    private void postEvent(int type, List<Marca> brands, List<TipoVehiculo> vehicleType,
//                           List<Modelo> models, List<Color> colors, List<Anio> years, String errorMessage) {
//        AddVehicleEvent addVehicleEvent = new AddVehicleEvent();
//        addVehicleEvent.setEventType(type);
//        if (brands != null)
//            addVehicleEvent.setMarcas(brands);
//        if (vehicleType != null)
//            addVehicleEvent.setVehiclesType(vehicleType);
//        if (models != null)
//            addVehicleEvent.setModelos(models);
//        if (colors != null)
//            addVehicleEvent.setColors(colors);
//        if (years != null)
//            addVehicleEvent.setYears(years);
//        if (errorMessage != null)
//            addVehicleEvent.setErrorMessage(errorMessage);
////        setNewVehicle(addVehicleEvent);
//        IEventBus eventBus = GreenRobotEventBus.getInstance();
//        eventBus.post(addVehicleEvent);
//    }

//    private  void setNewVehicle(AddVehicleEvent addVehicleEvent) {
////        int idServer =  if(mSharedPreferences.getString(ADD_VEHICLE_SERVER_ID, ""))?;
//        Vehiculo vehicle = new Vehiculo();
//    }
//
//    private  void postEvent(int type, String message) {
//        postEvent(type, null, null, null, null, null, message);
//    }
//
//    private  void postEventYears(int type, List<Anio> anios) {
//        postEvent(type, null, null, null, null, anios, null);
//    }
//
//    private  void postEventColors(int type, List<Color> colors) {
//        postEvent(type, null, null, null, colors, null, null);
//    }
//
//    private  void postEvent(int type) {
//        postEvent(type, null, null, null, null, null, null);
//    }
//
//    private  void postEventModels(int type, List<Modelo> models) {
//        postEvent(type, null, null, models, null, null, null);
//    }
//
//    private  void postEventVehiclesType(int type, List<TipoVehiculo> vehiclesType) {
//        postEvent(type, null, vehiclesType, null, null, null, null);
//    }
//
//    private  void postEventBrands(int type, List<Marca> brands) {
//        postEvent(type, brands, null, null, null, null, null);
//    }

    private void postEvent(int type, TipoCaja tipoCaja) {
        postEvent(type, null, tipoCaja, null, null, null);
    }

//    private void postEvent(int type, List<TipoVehiculo> vehiclesType, TipoCaja tipoCaja, String errorMessage) {
//        AddVehicleEvent addVehicleEvent = new AddVehicleEvent();
//        addVehicleEvent.setEventType(type);
//        if (vehiclesType != null)
//            addVehicleEvent.setVehiclesType(vehiclesType);
//        if (tipoCaja != null)
//            addVehicleEvent.setBoxType(tipoCaja);
//        if (errorMessage != null)
//            addVehicleEvent.setErrorMessage(errorMessage);
//        EventBus eventBus = GreenRobotEventBus.getInstance();
//        eventBus.post(addVehicleEvent);
//    }

    private void postEvent(int type, List<TipoVehiculo> vehiclesType, TipoCaja tipoCaja, List<String> transmissionTypeList, List<String> fuelTypeList, String errorMessage) {
        AddVehicleEvent addVehicleEvent = new AddVehicleEvent();
        addVehicleEvent.setEventType(type);
        if (vehiclesType != null)
            addVehicleEvent.setVehiclesType(vehiclesType);
        if (tipoCaja != null)
            addVehicleEvent.setBoxType(tipoCaja);
        if (transmissionTypeList != null)
            addVehicleEvent.setTransmissionType(transmissionTypeList);
        if (fuelTypeList != null)
            addVehicleEvent.setFuelType(fuelTypeList);
        if (errorMessage != null)
            addVehicleEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(addVehicleEvent);
    }

    private static void postEvent(int type, List<TipoVehiculo> vehiclesType, String errorMessage) {
        AddVehicleEvent addVehicleEvent = new AddVehicleEvent();
        addVehicleEvent.setEventType(type);
        if (vehiclesType != null)
            addVehicleEvent.setVehiclesType(vehiclesType);
        if (errorMessage != null)
            addVehicleEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(addVehicleEvent);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

}
