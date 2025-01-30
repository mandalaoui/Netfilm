package com.example.androidapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapp.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insertMovie(Movie movie);


//    @Query("SELECT * FROM movies")
//    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movies WHERE _id = :movieId")
    void deleteMovieById(String movieId);

    @Query("SELECT * FROM movies")
    List<Movie> index();
    @Query("SELECT * FROM movies WHERE _id = :movieId LIMIT 1")
    Movie getMovieById(String movieId);
    @Query("DELETE FROM movies")
    void clear();

    @Insert
    void insertList(List<Movie> movies);

}