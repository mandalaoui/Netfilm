package com.example.androidapp.entities;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ConverterMovie {

    @TypeConverter
    public static String fromCategoriesList(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(categories);
    }

    @TypeConverter
    public static List<Category> toCategoriesList(String data) {
        if (data == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(data, new TypeToken<List<Category>>() {}.getType());
    }

    @TypeConverter
    public static String fromNumber(Number number) {
        if (number == null) {
            return null;
        }
        return number.toString();
    }

    @TypeConverter
    public static Number toNumber(String data) {
        if (data == null) {
            return null;
        }
        try {
            return Integer.valueOf(data);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
