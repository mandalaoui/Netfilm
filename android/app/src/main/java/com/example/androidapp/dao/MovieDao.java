package com.example.androidapp.dao;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert
    void insertMovie(Movie movie);


//    @Query("SELECT * FROM movies")
//    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM Movie WHERE _id = :movieId")
    void deleteMovieById(String movieId);

//    @Query("SELECT * FROM movies")
//    List<Movie> index();
//    @Query("SELECT * FROM movies WHERE _id = :movieId LIMIT 1")
//    Movie getMovieById(String movieId);

    @Query("SELECT * FROM Movie")
    List<Movie> index();

//    @Query("SELECT * FROM movies WHERE id = :id")
//    Movie getMovieById(String id);

    @Insert
    void insert(Movie... movies);

    @Update
    void update(Movie... movies);

//    @Delete
//    void delete(Movie... movies);

    @Query("DELETE FROM Movie")
    void clear();

    @Insert
    void insertList(List<Movie> movies);

    @Query("SELECT * FROM Movie WHERE _id = :movieId")
    Movie getMovieById(String movieId);

}
