package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlantAdapter adapter;
    private List<Plant> plantList;
    private Button btnAddPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        initViews();
        setupRecyclerView();
        loadSampleData();

        btnAddPlant.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, AddPlantActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        btnAddPlant = findViewById(R.id.btn_add_plant);
    }

    private void setupRecyclerView() {
        plantList = new ArrayList<>();
        adapter = new PlantAdapter(this, plantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadSampleData() {
        plantList.add(new Plant("Daun Hijau", "Rp 200.000", "Tanaman ini berasal dari negara x, merupakan tanaman langka", R.drawable.ic_plant));
        plantList.add(new Plant("Monstera", "Rp 150.000", "Tanaman hias populer dengan daun berlubang", R.drawable.ic_plant));
        plantList.add(new Plant("Sansevieria", "Rp 75.000", "Tanaman lidah mertua yang mudah perawatan", R.drawable.ic_plant));
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("plant_name");
            String price = data.getStringExtra("plant_price");
            String description = data.getStringExtra("plant_description");

            plantList.add(new Plant(name, price, description, R.drawable.ic_plant));
            adapter.notifyDataSetChanged();
        }
    }
}