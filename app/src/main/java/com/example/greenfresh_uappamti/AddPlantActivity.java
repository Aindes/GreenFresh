package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlantActivity extends AppCompatActivity {

    private static final String TAG = "AddPlantActivity";

    private EditText etName, etPrice, etDescription;
    private Button btnSave, btnAdd;

    private PlantApiService apiService;
    private boolean isEditMode = false;
    private String currentPlantName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        apiService = ApiClient.getPlantApiService();
        initViews();
        checkEditMode();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_plant_name);
        etPrice = findViewById(R.id.et_plant_price);
        etDescription = findViewById(R.id.et_plant_description);
        btnSave = findViewById(R.id.btn_save);
        btnAdd = findViewById(R.id.btn_add);
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("edit_mode", false);

        if (isEditMode) {
            btnSave.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            btnSave.setText("Update");

            String name = intent.getStringExtra("plant_name");
            String price = intent.getStringExtra("price");
            String description = intent.getStringExtra("description");

            // Clean price from "Rp " prefix if exists
            if (price != null && price.startsWith("Rp ")) {
                price = price.substring(3).trim();
            }

            currentPlantName = name;
            etName.setText(name);
            etPrice.setText(price);
            etDescription.setText(description);

            // Disable name editing in edit mode to avoid conflicts
            etName.setEnabled(false);
        } else {
            btnSave.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    private void setupClickListeners() {
        btnSave.setOnClickListener(v -> savePlant());
        btnAdd.setOnClickListener(v -> savePlant());
    }

    private void savePlant() {
        String name = etName.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate price is numeric
        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Harga harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        Plant plant = new Plant(name, price, description);

        if (isEditMode) {
            updatePlant(plant);
        } else {
            createPlant(plant);
        }
    }

    private void createPlant(Plant plant) {
        Log.d(TAG, "Creating plant: " + plant.getName());

        Call<PlantResponse> call = apiService.createPlant(plant);
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                Log.d(TAG, "Create response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    PlantResponse plantResponse = response.body();
                    Log.d(TAG, "Create response message: " + plantResponse.getMessage());

                    // Check if operation was successful based on message
                    if (plantResponse.isOperationSuccessful()) {
                        Toast.makeText(AddPlantActivity.this, "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AddPlantActivity.this, "Gagal menambahkan tanaman: " + plantResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Gagal menambahkan tanaman";
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyStr = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBodyStr);
                            errorMessage += " (Server Error: " + response.code() + ")";
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(AddPlantActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Log.e(TAG, "Create plant failed", t);
                Toast.makeText(AddPlantActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlant(Plant plant) {
        Log.d(TAG, "Updating plant: " + currentPlantName);

        Call<PlantResponse> call = apiService.updatePlant(currentPlantName, plant);
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                Log.d(TAG, "Update response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    PlantResponse plantResponse = response.body();
                    Log.d(TAG, "Update response message: " + plantResponse.getMessage());

                    // Check if operation was successful based on message
                    if (plantResponse.isOperationSuccessful()) {
                        Toast.makeText(AddPlantActivity.this, "Tanaman berhasil diupdate", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AddPlantActivity.this, "Gagal mengupdate tanaman: " + plantResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Gagal mengupdate tanaman";
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyStr = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBodyStr);
                            errorMessage += " (Server Error: " + response.code() + ")";
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(AddPlantActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlantResponse> call, Throwable t) {
                Log.e(TAG, "Update plant failed", t);
                Toast.makeText(AddPlantActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}