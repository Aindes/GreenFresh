package com.example.greenfresh_uappamti;

import com.google.gson.annotations.SerializedName;

public class Plant {
    @SerializedName("plant_name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("description")
    private String description;

    private transient int imageResource;

    public Plant(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResource = R.drawable.ic_plant;
    }

    public Plant(String name, String price, String description, int imageResource) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResource = imageResource;
    }

    public Plant() {
        this.imageResource = R.drawable.ic_plant;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getImageResource() { return imageResource; }
    public void setImageResource(int imageResource) { this.imageResource = imageResource; }
}