package com.example.androidapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.Movie;

@Dao
public interface MovieDao {
    @Insert
    void insertMovie(Movie movie);

    @Query("SELECT * FROM movies WHERE _id = :movieId LIMIT 1")
    Movie getMovieById(String movieId);
}