package com.bzgroup.pitboxauxiliovehicular.menu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.bzgroup.pitboxauxiliovehicular.MainActivity;
import com.bzgroup.pitboxauxiliovehicular.R;
import com.bzgroup.pitboxauxiliovehicular.entities.Categorie;
import com.bzgroup.pitboxauxiliovehicular.entities.MainMenuItem;
import com.bzgroup.pitboxauxiliovehicular.services.ui.ServicesActivity;
import com.bzgroup.pitboxauxiliovehicular.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterMainMenu extends RecyclerView.Adapter<AdapterMainMenu.ViewHolder> {

    //    private List<MainMenuItem> mItems;
    private List<Categorie> mItems;
    private Context mContext;

    public AdapterMainMenu(Context context, List<Categorie> mItems) {
        this.mItems = mItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_main_menu_item, parent, false);
        return new ViewHolder(view);
    }

    public void add(List<Categorie> carts) {
        mItems.clear();
        mItems.addAll(carts);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        MainMenuItem item = mItems.get(position);
        Categorie item = mItems.get(position);
//        holder.fragment_main_menu_item_img.setImageDrawable(item.getImage());
        Glide.with(mContext)
                .load(Constants.IMAGE_URL + item.getImage())
                .into(holder.fragment_main_menu_item_img);
        holder.fragment_main_menu_item_txt.setText(item.getNombre());
        holder.fragment_main_menu_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ServicesActivity.class);
                intent.putExtra("CATEGORIE_ID", item.getId());
                intent.putExtra("IS_SCHEDULE", item.isProgramable());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fragment_main_menu_item_img)
        ImageView fragment_main_menu_item_img;
        @BindView(R.id.fragment_main_menu_item_txt)
        TextView fragment_main_menu_item_txt;
        @BindView(R.id.fragment_main_menu_container)
        ConstraintLayout fragment_main_menu_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
