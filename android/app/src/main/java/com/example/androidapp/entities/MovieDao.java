package com.example.androidapp.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    List<Movie> index();

//    @Query("SELECT * FROM category WHERE id = :id")
//    Category get(int id);

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
