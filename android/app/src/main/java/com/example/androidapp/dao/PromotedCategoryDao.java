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
    @Query("SELECT * FROM PromotedCategory")
    List<PromotedCategory> index();

//    @Query("SELECT * FROM category WHERE id = :id")
//    Category get(int id);

    @Insert
    void insert(PromotedCategory... categories);

    @Update
    void update(PromotedCategory... categories);

    @Delete
    void delete(PromotedCategory... categories);

    @Query("DELETE FROM PromotedCategory")
    void clear();

    @Insert
    void insertList(List<PromotedCategory> categories);
}
