package com.example.androidapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
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

    public void deleteMovieById(String movieId) {
        repository.deleteMovieById(movieId); // קורא למחיקה מתוך ה-Repository
    }

    public void add(Movie movie, File imageFile, File videoFile) {
        repository.add(movie, imageFile, videoFile);
    }
//
//    public void delete(Category category) {
//        repository.delete(category);
//    }

    public void reload() {
        Log.d("CategoriesViewModel", "Reloading categories...");
        repository.reload();
    }
//    private MovieRepository repository;
//    private LiveData<List<Movie>> allMovies;
//    private LiveData<List<Movie>> moviesFromApi;
//
//    public MovieViewModel(Application application) {
//        super(application);
//        repository = new MovieRepository(application);
//        allMovies = repository.getAllMovies();
//        moviesFromApi = repository.getMoviesFromApi();
//    }
//
//    public LiveData<List<Movie>> getAllMovies() {
//        return allMovies;
//    }
//
//    public LiveData<List<Movie>> getMoviesFromApi() {
//        return moviesFromApi;
//    }
//
//    public void insertCategory(Category category) {
//        repository.insertCategory(category);
//    }
//}
//}
//    public void fetchMoviesFromApi() {
//        movieRepository.fetchMoviesFromApi();
//    }





//    public LiveData<Movie> getMovie(String movieId, String userId) {
//        movieRepository.getMovie(movieId, userId, new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                if (response.isSuccessful()) {
//                    movieLiveData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//            }
//        });
//        return movieLiveData;
//    }
}
