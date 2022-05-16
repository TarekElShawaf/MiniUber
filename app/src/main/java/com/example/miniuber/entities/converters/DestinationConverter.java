package com.example.miniuber.entities.converters;

import androidx.room.TypeConverter;

import com.example.miniuber.entities.Location;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

public class DestinationConverter {
    @TypeConverter
    public static String convertDestinationToJson(LatLng location) {
        return new Gson().toJson(location);

    }
    //convert json classs to location
    @TypeConverter
    public static LatLng convertJsonToDestination(String json) {
        return new Gson().fromJson(json, LatLng.class);
    }
}
