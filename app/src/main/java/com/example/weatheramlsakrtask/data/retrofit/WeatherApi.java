package com.example.weatheramlsakrtask.data.retrofit;


import com.example.weatheramlsakrtask.data.weatherResponceModel.Response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather")
    Call<Response> getWeatherCondition(@Query("lat") double lat, @Query("lon") double lon,
                                       @Query("appid") String appid,
                                       @Query("units") String units);
}
