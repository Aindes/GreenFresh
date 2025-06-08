package com.example.greenfresh_uappamti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private Context context;
    private OnPlantActionListener listener;

    public interface OnPlantActionListener {
        void onDeletePlant(Plant plant, int position);
        void onDetailPlant(Plant plant);
    }

    public PlantAdapter(Context context, List<Plant> plantList, OnPlantActionListener listener) {
        this.context = context;
        this.plantList = plantList;
        this.listener = listener;
    }

    // Constructor untuk kompatibilitas dengan kode lama
    public PlantAdapter(Context context, List<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
        this.listener = null;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        holder.tvName.setText(plant.getName());

        // Format price
        String price = plant.getPrice();
        if (price != null && !price.startsWith("Rp")) {
            price = "Rp " + price;
        }
        holder.tvPrice.setText(price);

        holder.ivPlant.setImageResource(plant.getImageResource());

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeletePlant(plant, position);
            } else {
                // Fallback untuk kompatibilitas kode lama
                plantList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, plantList.size());
            }
        });

        holder.btnDetail.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailPlant(plant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPlant;
        TextView tvName, tvPrice;
        ImageButton btnDelete, btnDetail;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlant = itemView.findViewById(R.id.iv_plant);
            tvName = itemView.findViewById(R.id.tv_plant_name);
            tvPrice = itemView.findViewById(R.id.tv_plant_price);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}