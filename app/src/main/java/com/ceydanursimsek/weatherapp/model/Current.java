package com.ceydanursimsek.weatherapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Current {
    @SerializedName("temp")
    private double temp;
    @SerializedName("humidity")
    private double humidity;
    @SerializedName("weather")
    private List<Weather> weather;

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public List<Weather> getWeather() {
        return weather;
    }
}
