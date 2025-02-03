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
    public MovieViewModel() {
        repository = new MovieRepository();
        movies = repository.getAll();
    }

    public LiveData<List<Movie>> get() {
        Log.d("MovieViewModel", "movies list: " + movies.getValue());
        return movies;
    }

    public void deleteMovieById(Movie movie) {
        repository.deleteMovieById(movie);
    }

    public void add(Movie movie, File imageFile, File videoFile, File trailerFile) {
        repository.add(movie, imageFile, videoFile, trailerFile);
    }

    public void edit(String movieId,Movie movie, File imageFile, File videoFile,File trailerFile) {
        repository.edit(movieId,movie, imageFile, videoFile, trailerFile);
    }


    public void reload() {
        Log.d("CategoriesViewModel", "Reloading categories...");
        repository.reload();
    }

}
