package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlantDetailActivity extends AppCompatActivity {

    private ImageView ivPlant;
    private TextView tvName, tvPrice, tvDescription;
    private Button btnUpdate;

    private String plantName, plantPrice, plantDescription;
    private int plantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        initViews();
        loadPlantData();

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(PlantDetailActivity.this, AddPlantActivity.class);
            intent.putExtra("edit_mode", true);
            intent.putExtra("plant_name", plantName);
            intent.putExtra("price", plantPrice); // Changed key to "price" to match AddPlantActivity
            intent.putExtra("description", plantDescription);
            startActivityForResult(intent, 1);
        });
    }

    private void initViews() {
        ivPlant = findViewById(R.id.iv_plant);
        tvName = findViewById(R.id.tv_plant_name);
        tvPrice = findViewById(R.id.tv_plant_price);
        tvDescription = findViewById(R.id.tv_plant_description);
        btnUpdate = findViewById(R.id.btn_update);
    }

    private void loadPlantData() {
        Intent intent = getIntent();
        plantName = intent.getStringExtra("plant_name");
        plantPrice = intent.getStringExtra("plant_price");
        plantDescription = intent.getStringExtra("plant_description");
        plantImage = intent.getIntExtra("plant_image", R.drawable.ic_plant);

        tvName.setText(plantName);

        // Format price display
        String displayPrice = plantPrice;
        if (displayPrice != null && !displayPrice.startsWith("Rp")) {
            displayPrice = "Rp " + displayPrice;
        }
        tvPrice.setText(displayPrice);

        tvDescription.setText(plantDescription);
        ivPlant.setImageResource(plantImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Close this activity and return to list to refresh data
            setResult(RESULT_OK);
            finish();
        }
    }
}