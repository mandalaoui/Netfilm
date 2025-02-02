package com.example.androidapp.entities;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

public class ConverterMovie {

    @TypeConverter
    public static String fromCategoryList(List<String> categories) {
        if (categories == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String category : categories) {
            stringBuilder.append(category).append(",");
        }

        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    @TypeConverter
    public static List<String> toCategoryList(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return Arrays.asList(data.split(","));
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