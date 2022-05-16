package com.example.miniuber.entities.converters;

import androidx.room.TypeConverter;

import com.example.miniuber.entities.Location;
import com.google.gson.Gson;

public class PickupConverter {
    @TypeConverter
    public static String convertPickupToJson(Location location) {
        return new Gson().toJson(location);

    }
    //convert json classs to location
    @TypeConverter
    public static Location convertJsonToPickup(String json) {
        return new Gson().fromJson(json, Location.class);
    }
}
