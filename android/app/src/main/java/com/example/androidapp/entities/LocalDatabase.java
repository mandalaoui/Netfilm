package com.example.androidapp.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Category.class, Movie.class}, version = 2)
@TypeConverters(ConverterMovie.class)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase instance;
    public abstract CategoryDao categoryDao();

    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}