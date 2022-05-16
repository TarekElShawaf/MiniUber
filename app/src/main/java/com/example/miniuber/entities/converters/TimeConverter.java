package com.example.miniuber.entities.converters;
import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.sql.Time;
import java.util.Date;

public class TimeConverter {
    @TypeConverter
    public static Time convertJsonToTime(String json) {
        return new Gson().fromJson(json, Time.class);
    }
    @TypeConverter
    public static String convertTimeToJson(Time time) {
        return new Gson().toJson(time);
    }

}
