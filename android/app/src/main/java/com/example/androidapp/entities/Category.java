package com.example.androidapp.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


import java.util.List;

import java.util.List;
@Entity
@TypeConverters(ConverterCategory.class)
public class Category {
    @PrimaryKey
    @NonNull
    private String _id;
    @ColumnInfo(name = "movies")

    private List<String> movies;

    private boolean isPromoted;

    private String name ;


    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name ;
    }

    public void setName(String name ) {
        this.name = name ;
    }

    public boolean getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(boolean isPromoted) {
        this.isPromoted = isPromoted;
    }


    // Getter
    public List<String> getMovies() {
        return movies;
    }

    public Category(String name, boolean isPromoted, List<String> movies) {
        this.name = name;
        this.isPromoted = isPromoted;
        this.movies = movies;
    }
    public Category() {

    }

    // Setter
    public void setMovies(List<String> movies) {
        this.movies = movies;
    }
}
