package com.example.greenfresh_uappamti;

import com.google.gson.annotations.SerializedName;

public class CreateUpdateResponse {
    private String message;
    private DataWrapper data;

    public static class DataWrapper {
        private Plant plant;

        public Plant getPlant() {
            return plant;
        }

        public void setPlant(Plant plant) {
            this.plant = plant;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataWrapper getData() {
        return data;
    }

    public void setData(DataWrapper data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return message != null && message.contains("successfully");
    }
}