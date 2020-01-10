package com.bzgroup.pitboxauxiliovehicular.vehicles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.entities.vehicle.Vehicle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.ViewHolder> {

    private List<Vehicle> mItems;
    private Context mContext;

    public MyVehiclesAdapter(Context context, List<Vehicle> mItems) {
        this.mItems = mItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_vehicles_item, parent, false);
        return new ViewHolder(view);
    }

    public void add(List<Vehicle> carts) {
        mItems.clear();
        mItems.addAll(carts);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = mItems.get(position);

        holder.fragment_myvehicle_item_description.setText(vehicle.getAlias());
        holder.fragment_myvehicle_item_license_plate.setText(vehicle.getPlaca());
        holder.fragment_myvehicle_item_brand.setText(vehicle.getMarca());
        holder.fragment_myvehicle_item_model.setText(vehicle.getModelo());
        holder.fragment_myvehicle_item_year.setText(vehicle.getAnho());
//        holder.fragment_myvehicle_item_color.setText(vehicle.getco());
        holder.fragment_myvehicle_item_type_vehicle.setText(vehicle.getTipo_vehiculo_id());
        holder.activity_my_vehicles_item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "hola", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.activity_my_vehicles_item_container)
        ConstraintLayout activity_my_vehicles_item_container;

        @BindView(R.id.fragment_myvehicle_item_description)
        TextView fragment_myvehicle_item_description;

        @BindView(R.id.fragment_myvehicle_item_license_plate)
        TextView fragment_myvehicle_item_license_plate;

        @BindView(R.id.fragment_myvehicle_item_brand)
        TextView fragment_myvehicle_item_brand;

        @BindView(R.id.fragment_myvehicle_item_model)
        TextView fragment_myvehicle_item_model;

        @BindView(R.id.fragment_myvehicle_item_year)
        TextView fragment_myvehicle_item_year;

        @BindView(R.id.fragment_myvehicle_item_color)
        TextView fragment_myvehicle_item_color;

        @BindView(R.id.fragment_myvehicle_item_type_vehicle)
        TextView fragment_myvehicle_item_type_vehicle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
