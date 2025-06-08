package com.example.greenfresh_uappamti;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlantApiService {
    @GET("all")
    Call<PlantResponse> getPlants();

    @GET("{plant_name}")
    Call<PlantResponse> getPlant(@Path("plant_name") String name);

    @POST("new")
    Call<SimpleResponse> createPlant(@Body Plant plant);

    @PUT("{plant_name}")
    Call<SimpleResponse> updatePlant(@Path("plant_name") String name, @Body Plant plant);

    @DELETE("{plant_name}")
    Call<PlantResponse> deletePlant(@Path("plant_name") String name);
}