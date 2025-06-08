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

            if (price != null && price.startsWith("Rp ")) {
                price = price.substring(3).trim();
            }

            currentPlantName = name;
            etName.setText(name);
            etPrice.setText(price);
            etDescription.setText(description);

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
        Log.d(TAG, "Plant data - Name: " + plant.getName() + ", Price: " + plant.getPrice() + ", Description: " + plant.getDescription());

        Call<SimpleResponse> call = apiService.createPlant(plant);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Log.d(TAG, "Create response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    SimpleResponse createResponse = response.body();
                    Log.d(TAG, "Create response message: " + createResponse.getMessage());

                    if (createResponse.isSuccessful()) {
                        Toast.makeText(AddPlantActivity.this, "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AddPlantActivity.this, "Gagal menambahkan tanaman: " + createResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Gagal menambahkan tanaman (HTTP " + response.code() + ")";
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyStr = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBodyStr);
                            errorMessage = "Gagal menambahkan tanaman: " + errorBodyStr;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(AddPlantActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e(TAG, "Create plant failed", t);
                Toast.makeText(AddPlantActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlant(Plant plant) {
        Log.d(TAG, "Updating plant: " + currentPlantName);
        Log.d(TAG, "Plant data - Name: " + plant.getName() + ", Price: " + plant.getPrice() + ", Description: " + plant.getDescription());

        Call<SimpleResponse> call = apiService.updatePlant(currentPlantName, plant);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                Log.d(TAG, "Update response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    SimpleResponse updateResponse = response.body();
                    Log.d(TAG, "Update response message: " + updateResponse.getMessage());

                    if (updateResponse.isSuccessful()) {
                        Toast.makeText(AddPlantActivity.this, "Tanaman berhasil diupdate", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AddPlantActivity.this, "Gagal mengupdate tanaman: " + updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Gagal mengupdate tanaman (HTTP " + response.code() + ")";
                    try {
                        if (response.errorBody() != null) {
                            String errorBodyStr = response.errorBody().string();
                            Log.e(TAG, "Error body: " + errorBodyStr);
                            errorMessage = "Gagal mengupdate tanaman: " + errorBodyStr;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body", e);
                    }
                    Toast.makeText(AddPlantActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Log.e(TAG, "Update plant failed", t);
                Toast.makeText(AddPlantActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}