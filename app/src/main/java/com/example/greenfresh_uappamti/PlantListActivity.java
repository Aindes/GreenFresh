package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantListActivity extends AppCompatActivity {

    private static final String TAG = "PlantListActivity";

    private RecyclerView recyclerView;
    private PlantAdapter adapter;
    private List<Plant> plantList;
    private Button btnAddPlant;

    private FirebaseAuth mAuth;
    private PlantApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize API Service
        apiService = ApiClient.getPlantApiService();

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

        btnAddPlant.setOnClickListener(v -> {
            Intent intent = new Intent(PlantListActivity.this, AddPlantActivity.class);
            startActivityForResult(intent, 1);
        });

        // Welcome message
        String displayName = currentUser.getDisplayName();
        if (displayName != null && !displayName.isEmpty()) {
            Toast.makeText(this, "Selamat datang " + displayName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selamat datang", Toast.LENGTH_SHORT).show();
        }
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
        adapter = new PlantAdapter(this, plantList, new PlantAdapter.OnPlantActionListener() {
            @Override
            public void onDeletePlant(Plant plant, int position) {
                deletePlantFromApi(plant, position);
            }

            @Override
            public void onDetailPlant(Plant plant) {
                Intent intent = new Intent(PlantListActivity.this, PlantDetailActivity.class);
                intent.putExtra("plant_name", plant.getName());
                intent.putExtra("plant_price", plant.getPrice());
                intent.putExtra("plant_description", plant.getDescription());
                intent.putExtra("plant_image", plant.getImageResource());
                startActivityForResult(intent, 2); // Use different request code for detail
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadPlantsFromApi() {
        Log.d(TAG, "Loading plants from API");

        Call<PlantResponse> call = apiService.getPlants();
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                Log.d(TAG, "Load plants response code: " + response.code());

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    PlantResponse plantResponse = response.body();
                    plantList.clear();
                    plantList.addAll(plantResponse.getData());
                    adapter.notifyDataSetChanged();

                    Log.d(TAG, "Loaded " + plantList.size() + " plants");

                    if (plantList.isEmpty()) {
                        Toast.makeText(PlantListActivity.this, "Tidak ada data tanaman.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Failed to load plants - Response not successful or body is null");
                    Toast.makeText(PlantListActivity.this, "Gagal memuat data dari server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Log.e(TAG, "Load plants failed", t);
                Toast.makeText(PlantListActivity.this, "Koneksi Gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deletePlantFromApi(Plant plant, int position) {
        if (plant == null || plant.getName() == null) {
            Toast.makeText(this, "Informasi tanaman tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Deleting plant: " + plant.getName());

        Call<PlantResponse> call = apiService.deletePlant(plant.getName());
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                Log.d(TAG, "Delete response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    PlantResponse plantResponse = response.body();
                    Log.d(TAG, "Delete response message: " + plantResponse.getMessage());

                    // Check if deletion was successful based on message
                    if (plantResponse.isOperationSuccessful()) {
                        plantList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, plantList.size());
                        Toast.makeText(PlantListActivity.this, "Tanaman berhasil dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PlantListActivity.this, "Gagal menghapus: " + plantResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e(TAG, "Delete failed - Response not successful or body is null");
                    String errorMessage = "Gagal menghapus tanaman";
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyStr = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBodyStr);
                            errorMessage += " (Server Error: " + response.code() + ")";
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(PlantListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Log.e(TAG, "Delete plant failed", t);
                Toast.makeText(PlantListActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 1 || requestCode == 2) && resultCode == RESULT_OK) {
            // Refresh data dari API setelah menambah/update tanaman
            loadPlantsFromApi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data ketika kembali ke activity ini
        loadPlantsFromApi();
    }
}