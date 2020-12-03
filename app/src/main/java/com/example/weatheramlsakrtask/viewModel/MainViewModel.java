package com.example.weatheramlsakrtask.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.weatheramlsakrtask.data.retrofit.RetrofitClient;
import com.example.weatheramlsakrtask.data.weatherResponceModel.Response;

import retrofit2.Call;

public class MainViewModel extends ViewModel {


    RetrofitClient retrofitClient = RetrofitClient.getInstance();

    public Call<Response> getData(double latitude, double longitude) {
        return retrofitClient.getCurrentWeatherCondition(latitude, longitude);
    }

}
