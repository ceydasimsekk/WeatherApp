package com.ceydanursimsek.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("timezone")
    private String timezone;
    @SerializedName("timezone_offset")
    private double timezoneOffset;
    @SerializedName("current")
    private Current current;

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public double getTimezoneOffset() {
        return timezoneOffset;
    }

    public Current getCurrent() {
        return current;
    }
}
