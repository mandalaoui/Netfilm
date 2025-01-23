package com.example.androidapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Movie {
    @PrimaryKey
    private String name;
    private List<Category> categories;
    private String movie_time;
    private String image;
    private Number Publication_year;
    private String description;
    private Number age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
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

    public void setAge(Number age) {
        this.age = age;
    }

    public Movie(String name) {
        this.name = name;
    }

    public Movie(String name, List<Category> categories, String movie_time, String image, Number publication_year, String description, Number age) {
        this.name = name;
        this.categories = categories;
        this.movie_time = movie_time;
        this.image = image;
        this.Publication_year = publication_year;
        this.description = description;
        this.age = age;
    }
}
