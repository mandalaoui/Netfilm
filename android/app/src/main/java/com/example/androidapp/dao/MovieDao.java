package com.example.androidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.entities.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    List<Movie> index();

    @Query("SELECT * FROM Movie WHERE id = :id")
    Movie get(String id);

    @Insert
    void insert(Movie... movies);

    @Update
    void update(Movie... movies);

    @Delete
    void delete(Movie... movies);

    @Query("DELETE FROM Movie")
    void clear();

    @Insert
    void insertList(List<Movie> movies);
}
