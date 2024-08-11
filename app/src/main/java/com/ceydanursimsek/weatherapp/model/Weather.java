package com.ceydanursimsek.weatherapp.model;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("id")
    private int id;
    @SerializedName("main")
    private String main;
    @SerializedName("description")
    private String description;
    @SerializedName("icon")
    private String icon;

    public int getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    /*  {
    "lat": 39.89,
    "lon": 32.75,
    "timezone": "Europe/Istanbul",
    "timezone_offset": 10800,
    "current": {

        "temp": 296.96,

        "humidity": 53,

        "weather": [
            {
                "id": 800,
                "main": "Clear",
                "description": "clear sky",
                "icon": "01d"
            }
        ]
    }
}  */

}
