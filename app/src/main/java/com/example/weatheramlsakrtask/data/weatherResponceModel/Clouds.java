package com.example.weatheramlsakrtask.data.weatherResponceModel;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private int all;

    public int getAll() {
        return all;
    }
}