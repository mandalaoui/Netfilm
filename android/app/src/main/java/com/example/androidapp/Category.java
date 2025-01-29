package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.androidapp.entities.ConverterCategory;
import com.example.androidapp.entities.ConverterMovie;
import com.example.androidapp.entities.Movie;

import java.util.List;
@Entity
@TypeConverters(ConverterCategory.class)
public class Category {
    @PrimaryKey
    @NonNull
    private String _id;
    @ColumnInfo(name = "movies")

    private List<String> movies;

    private String isPromoted;
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

    public String getIsPromoted() {
        return isPromoted;
    }

    public void setIsPromoted(String isPromoted) {
        this.isPromoted = isPromoted;
    }

    // Constructor
//    public Category(List<String> movies) {
//        this.movies = movies;
//    }

    // Getter
    public List<String> getMovies() {
        return movies;
    }

    public Category(String name, String isPromoted, List<String> movies) {
        this.name = name;
        this.isPromoted = isPromoted;
        this.movies = movies;
    }

    // Setter
    public void setMovies(List<String> movies) {
        this.movies = movies;
    }
}
