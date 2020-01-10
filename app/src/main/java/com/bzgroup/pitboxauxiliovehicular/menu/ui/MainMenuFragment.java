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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.entities.MainMenuItem;
import com.bzgroup.pitboxauxiliovehicular.menu.CategoriesPresenter;
import com.bzgroup.pitboxauxiliovehicular.menu.ICategoriesPresenter;
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

public class MainMenuFragment extends Fragment implements CategoriesView {

    private static final String TAG = MainMenuFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.fragment_main_menu_rv)
    RecyclerView recyclerView;
    AdapterMainMenu mAdapter;
    @BindView(R.id.fragment_main_menu_progress)
    ProgressBar fragment_main_menu_progress;

    private ICategoriesPresenter mPresenter;

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

        mPresenter = new CategoriesPresenter(getContext(), this);
        mPresenter.onCreate();
        mPresenter.handleCategories();
    }

    private void setupRecycler() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupAdapter() {
        mAdapter = new AdapterMainMenu(getContext(), new ArrayList<>());
//        requestMenu();
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
        mPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        fragment_main_menu_progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        fragment_main_menu_progress.setVisibility(View.GONE);
    }

    @Override
    public void providerCategories(List<Categorie> categories) {
        mAdapter.add(categories);
    }

    @Override
    public void providerCategoriesEmpty() {
        Toast.makeText(getContext(), "Sin categorías.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCategoriesErrorMessage(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
