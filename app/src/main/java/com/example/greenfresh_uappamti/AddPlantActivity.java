package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddPlantActivity extends AppCompatActivity {

    private EditText etName, etPrice, etDescription;
    private Button btnSave, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_plant_name);
        etPrice = findViewById(R.id.et_plant_price);
        etDescription = findViewById(R.id.et_plant_description);
        btnSave = findViewById(R.id.btn_save);
        btnAdd = findViewById(R.id.btn_add);
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

        Intent resultIntent = new Intent();
        resultIntent.putExtra("plant_name", name);
        resultIntent.putExtra("plant_price", "Rp " + price);
        resultIntent.putExtra("plant_description", description);
        setResult(RESULT_OK, resultIntent);
        finish();

        Toast.makeText(this, "Tanaman berhasil ditambahkan", Toast.LENGTH_SHORT).show();
    }
}