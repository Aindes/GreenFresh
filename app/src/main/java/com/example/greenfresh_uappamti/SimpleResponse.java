package com.example.greenfresh_uappamti;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SimpleResponse {
    private String message;
    private JsonElement data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return message != null && (
                message.toLowerCase().contains("successfully") ||
                        message.toLowerCase().contains("berhasil")
        );
    }
}