package com.example.androidapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Movie;
import com.example.androidapp.repositories.MovieRepository;

import java.io.File;
import java.util.List;

public class MovieViewModel extends ViewModel {

    private MovieRepository repository;
    private LiveData<List<Movie>> movies;

    // Constructor to initialize the repository and fetch the list of movies
    public MovieViewModel() {
        repository = new MovieRepository();
        movies = repository.getAll();
    }

    // Getter method to return the LiveData of movies to be observed in UI
    public LiveData<List<Movie>> get() {
        return movies;
    }

    // Method to delete a movie by its ID, calls the repository method to handle the deletion
    public void deleteMovieById(Movie movie) {
        repository.deleteMovieById(movie);
    }

    // Method to add a new movie, along with image, video, and trailer files
    public void add(Movie movie, File imageFile, File videoFile, File trailerFile) {
        repository.add(movie, imageFile, videoFile, trailerFile);
    }

    // Method to edit an existing movie by its ID, along with image, video, and trailer files
    public void edit(String movieId,Movie movie, File imageFile, File videoFile,File trailerFile) {
        repository.edit(movieId,movie, imageFile, videoFile, trailerFile);
    }

    // Method to reload the movies data, typically used for refreshing the list of movies
    public void reload() {
        repository.reload();
    }

    // Method to fetch a specific movie by its ID
    public LiveData<Movie> getMovieById (String id) {
        return repository.getMovieById(id);
    }

}
