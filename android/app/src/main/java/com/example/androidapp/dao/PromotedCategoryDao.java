package com.example.androidapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapp.entities.PromotedCategory;

import java.util.List;

@Dao
public interface PromotedCategoryDao {

    // Retrieve all promoted categories from the PromotedCategory table
    @Query("SELECT * FROM PromotedCategory")
    List<PromotedCategory> index();

    // Insert one or more PromotedCategory entries into the PromotedCategory table
    @Insert
    void insert(PromotedCategory... categories);

    // Update one or more PromotedCategory entries in the PromotedCategory table
    @Update
    void update(PromotedCategory... categories);

    // Delete one or more PromotedCategory entries from the PromotedCategory table
    @Delete
    void delete(PromotedCategory... categories);

    // Clear all entries from the PromotedCategory table
    @Query("DELETE FROM PromotedCategory")
    void clear();

    // Insert a list of PromotedCategory entries into the PromotedCategory table
    @Insert
    void insertList(List<PromotedCategory> categories);
}
