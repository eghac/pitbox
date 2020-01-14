package com.bzgroup.pitboxauxiliovehicular.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.entities.order.Pedido;
import com.bzgroup.pitboxauxiliovehicular.entities.order.Proveedor;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;
import com.bzgroup.pitboxauxiliovehicular.lib.EventBus;
import com.bzgroup.pitboxauxiliovehicular.lib.GreenRobotEventBus;
import com.bzgroup.pitboxauxiliovehicular.services.event.ServicesEvent;
import com.bzgroup.pitboxauxiliovehicular.utils.AppPreferences;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bzgroup.pitboxauxiliovehicular.utils.Constants.GLOBAL_URL;

public class ServicesRepository implements IServicesRepository {

    private static final String TAG = ServicesRepository.class.getSimpleName();
    private static final String URL_MYVEHICLE = GLOBAL_URL + "clientes/";
    private static final String URL_MYADDRESS = GLOBAL_URL + "clientes/";
    private static final String URL_ADD_ADDRESS = GLOBAL_URL + "direcciones";
    private static final String URL_SERVICES = GLOBAL_URL + "categorias/";
    private static final String URL_ORDER = GLOBAL_URL + "pedidos";
    private static final String URL_ORDER_GET_SUPPLIER = GLOBAL_URL + "pedidos/";
    private static final String URL_ORDER_GET_SUPPLIER_LOCATION = GLOBAL_URL + "proveedores/";
    private Context mContext;
    private AppPreferences mPreferences;

    public ServicesRepository(Context context) {
        mContext = context;
        mPreferences = AppPreferences.getInstance(mContext);
    }

    @Override
    public void handleMyVehicles() {
        requestMyVehicles();
    }

    private void requestMyVehicles() {
        String userUuid = getUserUuid();
//        userUuid = "2";
        if (userUuid == null)
            return;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_MYVEHICLE + userUuid + "/vehiculos", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 200)
                                loadRequestMyVehicles(response);
                            else if (response.getInt("status") == 422)
                                postEvent(ServicesEvent.SERVICES_MYVEHICLES_EMPTY, null, null, null, null, response.getString("message"));
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
                        postEvent(ServicesEvent.SERVICES_MYVEHICLES_ERROR, null, null, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private String getUserUuid() {
        String userUuid = AppPreferences.getInstance(mContext).readString(AppPreferences.Keys.USER_UUID);
        if (userUuid == null || userUuid.isEmpty()) {
            // arreglar el id
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_ERROR, null, null, null, null, "Hubo un error al obtener mis vehículos.");
            return null;
        }
        return userUuid;
    }

    private void loadRequestMyVehicles(JSONObject response) throws JSONException {
        JSONArray array = response.getJSONArray("data");
        List<Vehicle> myVehicles = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject myVehicle = array.getJSONObject(i);
            JSONObject vehicleType = myVehicle.getJSONObject("tipo_vehiculo");
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
                    myVehicle.getString("tipo_vehiculo_id"),
                    vehicleType.getString("nombre")
            ));
        }
        if (!myVehicles.isEmpty())
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_SUCCESS, myVehicles, null, null, null, null);
        else
            postEvent(ServicesEvent.SERVICES_MYVEHICLES_EMPTY, null, null, null, null, "No tenés vehículos registrados.");
    }

    @Override
    public void handleServices(String categorieId) {
        requestServices(categorieId);
    }

    @Override
    public void handleMyAddress() {
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
                                    postEvent(ServicesEvent.SERVICES_ADDRESS_EMPTY, null, null, null, null, response.getString("message"));
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
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "handleMyAddress error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(ServicesEvent.SERVICES_ADDRESS_ERROR, null, null, null, null, error.getMessage());
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
            postEventAddress(ServicesEvent.SERVICES_ADDRESS_SUCCESS, addresses);
        else
            postEvent(ServicesEvent.SERVICES_ADDRESS_EMPTY, null, null, null, null, response.getString("message"));
    }

    @Override
    public void handleAddAddress(double latitude, double longitude, String description) {
        requestAddAddress(latitude, longitude, description);
    }

    @Override
    public void handleOrder(String vehicleId, String serviceId, double latitude, double longitude, String scheduleDate, String scheduleTime, String description) {
        String userId = getUserUuid();
        if (userId == null)
            return;
        requestOrder(userId, vehicleId, serviceId, latitude, longitude, scheduleDate, scheduleTime, description);
    }

    @Override
    public void handleGetSupplier(String orderId) {
        requestGetSupplier(orderId);
    }

    private void requestGetSupplier(String orderId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_ORDER_GET_SUPPLIER + orderId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 200:
                                    loadRequestOrderGetSupplier(response);
                                    break;
                                case 422:
                                    postEventError(ServicesEvent.SERVICES_ORDER_GET_SUPPLIER_ERROR, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEventError(ServicesEvent.SERVICES_ORDER_GET_SUPPLIER_ERROR, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEventError(ServicesEvent.SERVICES_ORDER_GET_SUPPLIER_ERROR, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void postEventError(int type, String errorMessage) {
        postEvent(type, null, null, null, null, errorMessage);
    }

    private void loadRequestOrderGetSupplier(JSONObject response) {
        Log.d(TAG, "loadRequestOrderGetSupplier: " + response.toString());
        try {
            JSONObject jsonObject = response.getJSONObject("data");
            String supplierId = jsonObject.getString("proveedor_id");
            String orderId = jsonObject.getString("id");
            if (supplierId == null || supplierId.equals("null")) {
                Handler mHandler = new Handler();
                mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestGetSupplier(orderId);
                    }
                }, 10000);
            } else {
                // SERVICES_ORDER_GET_SUPPLIER_SUCCESS
                // connecting with your provider
                postEventOrderGetSupplier(ServicesEvent.SERVICES_ORDER_GET_SUPPLIER_SUCCESS, supplierId, "Conectando con el proveedor...");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            postEventError(ServicesEvent.SERVICES_ORDER_GET_SUPPLIER_ERROR, e.getMessage());
        }
    }

    @Override
    public void handleSupplierLocation(String supplierId) {
        requestSupplierLocation(supplierId);
    }

    private void requestSupplierLocation(String supplierId) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_ORDER_GET_SUPPLIER_LOCATION + supplierId + "/ubicacion", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 200:
                                    loadRequestOrderGetSupplierLocation(response);
                                    break;
                                case 422:
                                    postEventOrder(ServicesEvent.SERVICES_ORDER_GET_LOCATION_SUPPLIER_ERROR, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEvent(ServicesEvent.SERVICES_ORDER_GET_LOCATION_SUPPLIER_ERROR, null, null, null, null, e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEvent(ServicesEvent.SERVICES_ORDER_GET_LOCATION_SUPPLIER_ERROR, null, null, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestOrderGetSupplierLocation(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("data");
            JSONObject photo = object.getJSONObject("foto");
            Proveedor supplier = new Proveedor(
                    object.getString("id"),
                    object.getString("nombre_perfil"),
                    object.getString("latitud"),
                    object.getString("longitud"),
                    photo.getString("url"));
            postEventOrderGetSupplierLocation(ServicesEvent.SERVICES_ORDER_GET_LOCATION_SUPPLIER_SUCCESS, supplier, response.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            postEventOrder(ServicesEvent.SERVICES_ORDER_GET_LOCATION_SUPPLIER_ERROR, e.getMessage());
        }
    }

    private void postEventOrderGetSupplierLocation(int type, Proveedor supplier, String message) {
        postEventSupplier(type, supplier, message);
    }

    private void postEventOrderGetSupplier(int type, String supplierId, String message) {
        postEvent(type, null, null, null, null, supplierId, message);
    }

    private void requestOrder(String userId, String vehicleId, String serviceId, double latitude, double longitude, String scheduleDate, String scheduleTime, String description) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("cliente_id", userId);
        data.put("vehiculo_id", vehicleId);
        data.put("servicio_id", serviceId);
        data.put("latitud", latitude);
        data.put("longitud", longitude);
        data.put("fecha_programacion", scheduleDate);
        data.put("hora_programacion", scheduleTime);
        data.put("descripcion", description);
        Log.d(TAG, "requestOrder: data " + data.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_ORDER, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("status")) {
                                case 200:
                                    loadRequestOrder(response);
                                    break;
                                case 422:
                                    postEventOrder(ServicesEvent.SERVICES_ORDER_ERROR, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEvent(ServicesEvent.SERVICES_ORDER_ERROR, null, null, null, null, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        postEvent(ServicesEvent.SERVICES_ORDER_ERROR, null, null, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestOrder(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");
            Pedido order = new Pedido(
                    data.getString("id"),
                    data.getString("cliente_id"),
                    data.getString("vehiculo_id"),
                    data.getString("servicio_id"),
                    data.getString("latitud"),
                    data.getString("longitud"),
                    data.getString("fecha_programacion"),
                    data.getString("hora_programacion"),
                    data.getString("estado"),
                    data.getString("descripcion"),
                    data.getString("fecha_inicio"),
                    data.getString("hora_inicio"),
                    data.getString("precio_base"),
                    data.getString("precio_total"),
                    data.getString("created_at"),
                    data.getString("updated_at")
            );
            mPreferences.writeString(AppPreferences.Keys.ORDER_ID, order.getId());
            mPreferences.writeString(AppPreferences.Keys.ORDER_VEHICLE_ID, order.getVehiculo_id());
            mPreferences.writeString(AppPreferences.Keys.ORDER_SERVICE_ID, order.getServicio_id());
            mPreferences.writeString(AppPreferences.Keys.ORDER_LATITUDE, order.getLatitud());
            mPreferences.writeString(AppPreferences.Keys.ORDER_LONGITUDE, order.getLongitud());
            mPreferences.writeString(AppPreferences.Keys.ORDER_SCHEDULE_DATE, order.getFecha_programacion());
            mPreferences.writeString(AppPreferences.Keys.ORDER_SCHEDULE_TIME, order.getHora_programacion());
            mPreferences.writeString(AppPreferences.Keys.ORDER_STATE, order.getEstado());
            mPreferences.writeString(AppPreferences.Keys.ORDER_DESCRIPTION, order.getDescripcion());
            mPreferences.writeString(AppPreferences.Keys.ORDER_INIT_DATE, order.getFecha_inicio());
            mPreferences.writeString(AppPreferences.Keys.ORDER_INIT_TIME, order.getHora_inicio());
            mPreferences.writeString(AppPreferences.Keys.ORDER_BASE_PRICE, order.getPrecio_base());
            mPreferences.writeString(AppPreferences.Keys.ORDER_TOTAL_PRICE, order.getPrecio_total());
            mPreferences.writeString(AppPreferences.Keys.ORDER_CREATED_AT, order.getCreated_at());
            mPreferences.writeString(AppPreferences.Keys.ORDER_UPDATED_AT, order.getUpdated_at());
            postEventOrder(ServicesEvent.SERVICES_ORDER_SUCCESS, order, response.getString("message"));
        } catch (JSONException e) {
            e.printStackTrace();
            postEventOrder(ServicesEvent.SERVICES_ORDER_ERROR, e.getMessage());
        }
    }

    private void postEventOrder(int type, String message) {
        postEvent(type, null, null, null, null, message);
    }

    private void postEventOrder(int type, Pedido order, String message) {
        postEvent(type, null, null, null, order, message);
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
                                    postEvent(ServicesEvent.SERVICES_ADD_ADDRESS_ERROR, null, null, null, null, response.getString("message"));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            postEvent(ServicesEvent.SERVICES_ADD_ADDRESS_ERROR, null, null, null, null, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: ", error);
                        Toast.makeText(mContext, "requestAddAddress error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        postEvent(ServicesEvent.SERVICES_ADD_ADDRESS_ERROR, null, null, null, null, error.getMessage());
                    }
                }
        );
        SingletonVolley.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestAddAddress(JSONObject response) throws JSONException {
        Log.d(TAG, "loadRequestAddAddress: " + response.toString());
        postEvent(ServicesEvent.SERVICES_ADD_ADDRESS_SUCCESS, null, null, null, null, response.getString("message"));
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
                        postEvent(ServicesEvent.SERVICES_ERROR, null, null, null, null, error.getMessage());
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
//            JSONObject image = service.getJSONObject("icono");
            services.add(new Service(
                    service.getString("id"),
                    service.getString("nombre"),
                    service.getString("descripcion"),
                    service.getString("precio_base"),
                    service.getString("incremento_horario"),
//                    image.getString("url")
                    ""
            ));
        }
        if (!services.isEmpty())
            postEvent(ServicesEvent.SERVICES_SUCCESS, null, services, null, null, null);
        else
            postEvent(ServicesEvent.SERVICES_EMPTY, null, null, null, null, "No tenés vehículos registrados.");
    }


    private static void postEventMyVehicles(int type, List<Vehicle> myVehicles) {
        postEvent(type, myVehicles, null, null, null, null);
    }

    private static void postEventServices(int type, List<Service> services) {
        postEvent(type, null, services, null, null, null);
    }

    private void postEventAddress(int eventType, List<Address> addresses) {
        postEvent(eventType, null, null, addresses, null, null);
    }

    private static void postEventSupplier(int type, Proveedor supplier, String errorMessage) {
        ServicesEvent servicesEventEvent = new ServicesEvent();
        servicesEventEvent.setEventType(type);
        if (supplier != null)
            servicesEventEvent.setSupplier(supplier);
        if (errorMessage != null)
            servicesEventEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(servicesEventEvent);
    }

    private static void postEvent(int type, List<Vehicle> myVehicles, List<Service> services, List<Address> addresses, Pedido order, String errorMessage) {
        ServicesEvent servicesEventEvent = new ServicesEvent();
        servicesEventEvent.setEventType(type);
        if (order != null)
            servicesEventEvent.setOrder(order);
        if (addresses != null)
            servicesEventEvent.setAddresses(addresses);
        if (myVehicles != null)
            servicesEventEvent.setMyVehicles(myVehicles);
        if (services != null)
            servicesEventEvent.setServices(services);
        if (errorMessage != null)
            servicesEventEvent.setErrorMessage(errorMessage);
        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(servicesEventEvent);
    }

    private static void postEvent(int type, List<Vehicle> myVehicles, List<Service> services, List<Address> addresses, Pedido order, String supplierId, String errorMessage) {
        ServicesEvent servicesEventEvent = new ServicesEvent();
        servicesEventEvent.setEventType(type);
        if (supplierId != null)
            servicesEventEvent.setSupplierId(supplierId);
        if (order != null)
            servicesEventEvent.setOrder(order);
        if (addresses != null)
            servicesEventEvent.setAddresses(addresses);
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
