package com.example.androidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    // Query to get all categories from the Category table
    @Query("SELECT * FROM Category")
    List<Category> index();

    // Insert one category into the database
    @Insert
    void insert(Category... categories);

    // Update one category in the database
    @Update
    void update(Category... categories);

    // Delete one category from the database
    @Delete
    void delete(Category... categories);

    // Query to clear all data from the Category table
    @Query("DELETE FROM Category")
    void clear();

    // Insert a list of categories into the database
    @Insert
    void insertList(List<Category> categories);
}
