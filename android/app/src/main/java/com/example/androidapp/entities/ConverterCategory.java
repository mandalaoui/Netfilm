package com.example.androidapp.entities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ConverterCategory {

    // Converts a List<Movie> to a JSON string for storage in the database
    @TypeConverter
    public static String fromMoviesList(List<Movie> movies) {
        if (movies == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(movies);
    }

    // Converts a JSON string to a List<Movie> when retrieving from the database
    @TypeConverter
    public static List<Movie> toMoviesList(String data) {
        if (data == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<List<Movie>>() {}.getType());
    }
}