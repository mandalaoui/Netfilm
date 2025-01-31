package com.example.androidapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity
@TypeConverters(ConverterMovie.class)
public class Movie {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private List<String> categories;
    private String movie_time;
    private String image;
    private Number Publication_year;
    private String description;
    private Number age;
    private String video;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getMovie_time() {
        return movie_time;
    }

    public void setMovie_time(String movie_time) {
        this.movie_time = movie_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Number getPublication_year() {
        return Publication_year;
    }

    public void setPublication_year(Number publication_year) {
        Publication_year = publication_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Number getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(Number age) {
        this.age = age;
    }

    public Movie(String name) {
        this.name = name;
    }

    public Movie(String id, String name, List<String> categories, String movie_time, String image, Number publication_year, String description, Number age) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.movie_time = movie_time;
        this.image = image;
        this.Publication_year = publication_year;
        this.description = description;
        this.age = age;
    }

    public Movie(@NonNull String id, String name, List<String> categories, String movie_time, String image, Number publication_year, String description, Number age, String video) {
        this.id = id;
        this.name = name;
        this.categories = categories;
        this.movie_time = movie_time;
        this.image = image;
        Publication_year = publication_year;
        this.description = description;
        this.age = age;
        this.video = video;
    }
}
