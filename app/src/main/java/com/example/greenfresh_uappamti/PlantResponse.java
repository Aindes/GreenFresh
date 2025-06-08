package com.example.greenfresh_uappamti;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlantResponse {
    private boolean success;
    private String message;
    private List<Plant> data;
    private Plant plant;
    private CreateUpdateData data2;

    public static class CreateUpdateData {
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

    public CreateUpdateData getData2() {
        return data2;
    }

    public Plant getSinglePlant() {
        if (plant != null) {
            return plant;
        }
        if (data2 != null && data2.getPlant() != null) {
            return data2.getPlant();
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

    public void setData2(CreateUpdateData data2) {
        this.data2 = data2;
    }

    public boolean isOperationSuccessful() {
        return message != null && (
                message.contains("successfully") ||
                        message.contains("berhasil") ||
                        success
        );
    }
}