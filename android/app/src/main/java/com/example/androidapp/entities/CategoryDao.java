package com.example.androidapp.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    List<Category> index();

//    @Query("SELECT * FROM category WHERE id = :id")
//    Category get(int id);

    @Insert
    void insert(Category... categories);

    @Update
    void update(Category... categories);

    @Delete
    void delete(Category... categories);

    @Query("DELETE FROM category")
    void clear();

    @Insert
    void insertList(List<Category> categories);

}
