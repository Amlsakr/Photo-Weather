package com.example.weatheramlsakrtask.data.retrofit;


import com.example.weatheramlsakrtask.data.weatherResponceModel.Response;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static final String API_KEY = "2693f4dd7922bd3018b5e9213229e726";
    public static final String UNITS = "metric";
    public static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private static WeatherApi weatherApi;
    private static RetrofitClient INSTANCE;

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RetrofitClient();
            weatherApi =
                    new Retrofit.Builder()
                            .baseUrl(WEATHER_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                            .create(WeatherApi.class);
        }
        return INSTANCE;
    }

    public WeatherApi getMoviesApi() {
        return weatherApi;
    }

    public Call<Response> getCurrentWeatherCondition(double latitude, double longitude) {
        return weatherApi.getWeatherCondition(latitude, longitude, RetrofitClient.API_KEY, RetrofitClient.UNITS);
    }
}
