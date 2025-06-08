package com.example.greenfresh_uappamti;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private List<Plant> plantList;
    private Context context;

    public PlantAdapter(Context context, List<Plant> plantList) {
        this.context = context;
        this.plantList = plantList;
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
        holder.tvPrice.setText(plant.getPrice());
        holder.ivPlant.setImageResource(plant.getImageResource());

        holder.btnDelete.setOnClickListener(v -> {
            plantList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, plantList.size());
            Toast.makeText(context, "Tanaman dihapus", Toast.LENGTH_SHORT).show();
        });

        holder.btnDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantDetailActivity.class);
            intent.putExtra("plant_name", plant.getName());
            intent.putExtra("plant_price", plant.getPrice());
            intent.putExtra("plant_description", plant.getDescription());
            intent.putExtra("plant_image", plant.getImageResource());
            context.startActivity(intent);
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