package com.ceydanursimsek.weatherapp.service;

import com.ceydanursimsek.weatherapp.model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("onecall")
        Call<WeatherResponse> getCurrentWeather(
                @Query("lat") double lat,
                @Query("lon") double lon,
                @Query("appid") String apiKey

                );
}
