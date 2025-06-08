package com.example.greenfresh_uappamti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlantDetailActivity extends AppCompatActivity {

    private ImageView ivPlant;
    private TextView tvName, tvPrice, tvDescription;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        initViews();
        loadPlantData();

        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(PlantDetailActivity.this, AddPlantActivity.class);
            intent.putExtra("edit_mode", true);
            intent.putExtra("plant_name", tvName.getText().toString());
            intent.putExtra("plant_price", tvPrice.getText().toString());
            intent.putExtra("plant_description", tvDescription.getText().toString());
            startActivity(intent);
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
        String name = intent.getStringExtra("plant_name");
        String price = intent.getStringExtra("plant_price");
        String description = intent.getStringExtra("plant_description");
        int imageRes = intent.getIntExtra("plant_image", R.drawable.ic_plant);

        tvName.setText(name);
        tvPrice.setText(price);
        tvDescription.setText(description);
        ivPlant.setImageResource(imageRes);
    }
}