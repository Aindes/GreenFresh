package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class PlantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlantAdapter adapter;
    private List<Plant> plantList;
    private Button btnAddPlant;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User not logged in, redirect to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        loadSampleData();

        btnAddPlant.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, AddPlantActivity.class);
            startActivityForResult(intent, 1);
        });

        // Welcome message
        Toast.makeText(this, "Selamat datang " + currentUser.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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