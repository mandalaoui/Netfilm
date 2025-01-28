package com.example.androidapp.entities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ConverterCategory {

    @TypeConverter
    public static String fromMoviesList(List<Movie> movies) {
        if (movies == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(movies);
    }

    @TypeConverter
    public static List<Movie> toMoviesList(String data) {
        if (data == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<List<Movie>>() {}.getType());
    }
}

