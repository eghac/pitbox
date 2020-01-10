package com.bzgroup.pitboxauxiliovehicular.services.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.bzgroup.pitboxauxiliovehicular.entities.Address;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.services.ui.ServicesActivity;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;
import com.bzgroup.pitboxauxiliovehicular.utils.IOnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterMyAddress extends RecyclerView.Adapter<AdapterMyAddress.ViewHolder> {

    private List<Address> mItems;
    private Context mContext;
    private boolean isMyAddressScreen;
    private IOnItemClickListener mListener;

    public AdapterMyAddress(Context context, List<Address> mItems, boolean isMyAddressScreen, IOnItemClickListener listener) {
        this.mItems = mItems;
        mContext = context;
        this.isMyAddressScreen = isMyAddressScreen;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bs_my_address_item, parent, false);
        return new ViewHolder(view);
    }

    public void add(List<Address> addresses) {
        mItems.clear();
        mItems.addAll(addresses);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address item = mItems.get(position);
        if (isMyAddressScreen) {
            holder.bs_my_address_item_delete_btn.setVisibility(View.VISIBLE);
            holder.bs_my_address_item_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("MY_ADDRESS_TYPE", 2);
                    intent.putExtra("MY_ADDRESS_ITEM", item);
                    mListener.onItemClick(intent);
                }
            });

            holder.bs_my_address_item_edit_btn.setVisibility(View.VISIBLE);
            holder.bs_my_address_item_edit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("MY_ADDRESS_TYPE", 1);
                    intent.putExtra("MY_ADDRESS_ITEM", item);
                    mListener.onItemClick(intent);
                }
            });
        } else {
            holder.bs_my_address_item_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(item);
                }
            });
        }
        holder.bs_my_address_item_description.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bs_my_address_item_description)
        TextView bs_my_address_item_description;
        @BindView(R.id.bs_my_address_item_container)
        ConstraintLayout bs_my_address_item_container;
        @BindView(R.id.bs_my_address_item_edit_btn)
        ImageView bs_my_address_item_edit_btn;
        @BindView(R.id.bs_my_address_item_delete_btn)
        ImageView bs_my_address_item_delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
