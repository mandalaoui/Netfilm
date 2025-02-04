package com.example.androidapp.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.dao.CategoryDao;
import com.example.androidapp.dao.PromotedCategoryDao;

// Annotating the class as a Room Database. Specifies the entities (tables) and the version of the database
@Database(entities = {PromotedCategory.class, Movie.class, Category.class}, version = 9)
@TypeConverters(ConverterMovie.class)
public abstract class LocalDatabase extends RoomDatabase {

    // Abstract methods for accessing DAOs (Data Access Objects) for each entity (table)
    private static LocalDatabase instance;
    public abstract PromotedCategoryDao promotedCategoryDao();
    public abstract CategoryDao categoryDao();
    public abstract MovieDao movieDao();

    // Provides a singleton instance of the LocalDatabase.
    // Synchronized to ensure that the database instance is created only once
    public static synchronized LocalDatabase getInstance(Context context) {
        if (instance == null) {
            // Creates the database using Room's database builder
            // Provides a fallback strategy for migration (destructive migration)
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, "local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}