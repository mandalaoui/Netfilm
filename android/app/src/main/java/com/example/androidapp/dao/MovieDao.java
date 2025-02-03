package com.example.androidapp.dao;

import androidx.lifecycle.LiveData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    // Insert a single Movie into the Movie table
    @Insert
    void insertMovie(Movie movie);

    // Retrieve all movies from the Movie table
    @Query("SELECT * FROM Movie")
    List<Movie> index();

    // Update one Movie in the Movie table
    @Update
    void update(Movie... movies);

    // Delete one Movie from the Movie table
    @Delete
    void delete(Movie... movies);

    // Clear all Movies from the Movie table
    @Query("DELETE FROM Movie")
    void clear();

    // Insert a list of Movies into the Movie table, ignoring conflicts (duplicate entries)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertList(List<Movie> movies);

    // Retrieve a Movie by its ID from the Movie table
    @Query("SELECT * FROM Movie WHERE _id = :movieId")
    Movie getMovieById(String movieId);

}
