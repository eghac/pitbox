package com.bzgroup.pitboxauxiliovehicular.menu.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.entities.MainMenuItem;
import com.bzgroup.pitboxauxiliovehicular.menu.adapter.AdapterMainMenu;
import com.bzgroup.pitboxauxiliovehicular.utils.SingletonVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bzgroup.pitboxauxiliovehicular.utils.Constants.GLOBAL_URL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainMenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainMenuFragment extends Fragment {

    private static final String TAG = MainMenuFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.fragment_main_menu_rv)
    RecyclerView recyclerView;
    AdapterMainMenu mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupAdapter();
        setupRecycler();
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupAdapter() {

        requestMenu();

        List<MainMenuItem> mItems = new ArrayList<>();
        mItems.add(new MainMenuItem(getResources().getDrawable(R.drawable.ic_grupo_957), "Auxilio mecánico"));
        mItems.add(new MainMenuItem(getResources().getDrawable(R.drawable.ic_grupo_956), "Conductor designado"));
        mItems.add(new MainMenuItem(getResources().getDrawable(R.drawable.ic_grupo_946), "Lavado a domicilio"));
        mItems.add(new MainMenuItem(getResources().getDrawable(R.drawable.ic_grupo_973), "Mantenimiento a domicilio"));
        mItems.add(new MainMenuItem(getResources().getDrawable(R.drawable.ic_grupo_974), "Estaciones de servicio"));
        mAdapter = new AdapterMainMenu(getContext(), new ArrayList<>());
    }

    private static final String URL_MENU = GLOBAL_URL + "categorias";

    private void requestMenu() {
        Log.d(TAG, "requestMenu: ");
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL_MENU, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: ");
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
                    }
                }
        );
        SingletonVolley.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void loadRequestCategories(JSONArray response) throws JSONException {
        List<Categorie> categories = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject categorie = response.getJSONObject(i);
            JSONObject image = categorie.getJSONObject("icono");
            categories.add(new Categorie(
                    categorie.getString("nombre"),
                    image.getString("url")
            ));
        }
        if (categories.size() > 0) {
            Log.d(TAG, "loadRequestCategories: " + categories.size());
            mAdapter.add(categories);
        } else {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
