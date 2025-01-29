package com.example.androidapp;

import android.content.Context;

import androidx.databinding.adapters.Converters;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.androidapp.entities.ConverterMovie;
import com.example.androidapp.entities.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Database(entities = {Movie.class, Category.class}, version = 1)
@TypeConverters({ConverterMovie.class})
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao movieDao();
}
//@Database(entities = {Movie.class}, version = 1, exportSchema = false)
//public abstract class MovieDatabase extends RoomDatabase {
//
//    public abstract MovieDao movieDao();  // הגדרת ה-DAO
//
//    private static volatile MovieDatabase INSTANCE;  // מבנה Singleton
//    private static final int NUMBER_OF_THREADS = 4;
//    public static final ExecutorService databaseWriteExecutor =
//            Executors.newFixedThreadPool(NUMBER_OF_THREADS);  // Executor לביצוע פעולות אסינכרוניות
//
//    public static MovieDatabase getInstance(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (MovieDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                                    MovieDatabase.class, "movie_database")
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//}


