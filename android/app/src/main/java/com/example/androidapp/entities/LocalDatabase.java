package com.example.androidapp.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidapp.dao.CategoryDao;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.dao.PromotedCategoryDao;

@Database(entities = {PromotedCategory.class, Movie.class, Category.class}, version = 6)
@TypeConverters(ConverterMovie.class)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase instance;
    public abstract PromotedCategoryDao promotedCategoryDao();
    public abstract CategoryDao categoryDao();

    public abstract MovieDao movieDao();

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