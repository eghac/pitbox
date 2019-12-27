package com.bzgroup.pitboxauxiliovehicular.services.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.entities.Service;
import com.bzgroup.pitboxauxiliovehicular.services.ui.ServicesActivity;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;
import com.bzgroup.pitboxauxiliovehicular.utils.IOnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.bumptech.glide.Glide;

public class AdapterServices extends RecyclerView.Adapter<AdapterServices.ViewHolder> {

    //    private List<MainMenuItem> mItems;
    private List<Service> mItems;
    private Context mContext;
    private IOnItemClickListener mlistener;

    public AdapterServices(Context context, List<Service> mItems, IOnItemClickListener listener) {
        this.mItems = mItems;
        mContext = context;
        mlistener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_services_item, parent, false);
        return new ViewHolder(view);
    }

    public void add(List<Service> carts) {
        mItems.clear();
        mItems.addAll(carts);
        notifyDataSetChanged();
    }

    int selectedPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = mItems.get(position);

        if (selectedPosition == position)
            holder.activity_services_container.setBackgroundColor(Color.parseColor("#444F63"));
        else
            holder.activity_services_container.setBackgroundColor(Color.parseColor("#353A50"));

        Glide.with(mContext)
                .load(Constants.IMAGE_URL + service.getIcono())
                .into(holder.activity_services_icon);
        holder.activity_services_name.setText(service.getNombre());
        holder.activity_services_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                mlistener.onItemClick(service);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.activity_services_icon)
        ImageView activity_services_icon;
        @BindView(R.id.activity_services_name)
        TextView activity_services_name;
        @BindView(R.id.activity_services_container)
        ConstraintLayout activity_services_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
