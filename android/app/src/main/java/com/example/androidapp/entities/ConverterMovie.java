package com.example.androidapp.entities;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

public class ConverterMovie {

    // Converts a List of category strings to a comma-separated String for storage in the database
    @TypeConverter
    public static String fromCategoryList(List<String> categories) {
        if (categories == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (String category : categories) {
            stringBuilder.append(category).append(",");
        }

        // Remove the last comma if the list is not empty
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

    // Converts a comma-separated String back into a List of category strings
    @TypeConverter
    public static List<String> toCategoryList(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return Arrays.asList(data.split(","));
    }

    // Converts a Number object to a String for storage
    @TypeConverter
    public static String fromNumber(Number number) {
        if (number == null) {
            return null;
        }
        return number.toString();
    }

    // Converts a String back into a Number (specifically an Integer in this case)
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