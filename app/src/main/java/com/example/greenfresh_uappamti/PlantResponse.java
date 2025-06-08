package com.example.greenfresh_uappamti;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantResponse {
    private boolean success;
    private String message;
    private List<Plant> data;  // untuk GET all plants
    private Plant plant;       // untuk single plant operations
    private PlantData plantData; // untuk nested plant data

    // Inner class untuk handle nested plant data structure
    public static class PlantData {
        private Plant plant;

        public Plant getPlant() {
            return plant;
        }

        public void setPlant(Plant plant) {
            this.plant = plant;
        }
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<Plant> getData() {
        return data;
    }

    public Plant getPlant() {
        return plant;
    }

    public PlantData getPlantData() {
        return plantData;
    }

    // Method untuk mendapatkan plant dari berbagai struktur response
    public Plant getSinglePlant() {
        if (plant != null) {
            return plant;
        }
        if (plantData != null && plantData.getPlant() != null) {
            return plantData.getPlant();
        }
        if (data != null && !data.isEmpty()) {
            return data.get(0);
        }
        return null;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(List<Plant> data) {
        this.data = data;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public void setPlantData(PlantData plantData) {
        this.plantData = plantData;
    }

    // Helper method untuk check apakah response berhasil
    public boolean isOperationSuccessful() {
        return message != null && (
                message.contains("successfully") ||
                        message.contains("berhasil") ||
                        success
        );
    }
}