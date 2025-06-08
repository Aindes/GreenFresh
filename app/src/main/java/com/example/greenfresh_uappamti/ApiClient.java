package com.example.greenfresh_uappamti;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Perbaikan: Tambahkan prefix /plant/ ke BASE_URL
    private static final String BASE_URL = "https://uappam.kuncipintu.my.id/plant/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static PlantApiService getPlantApiService() {
        return getClient().create(PlantApiService.class);
    }
}