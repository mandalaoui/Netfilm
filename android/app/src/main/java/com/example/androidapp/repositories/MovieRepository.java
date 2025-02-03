package com.example.androidapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.entities.LocalDatabase;
import com.example.androidapp.entities.Movie;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private MovieDao dao;
    private MovieListData movieiListData;
    private MovieApi api;

    // Constructor that initializes the repository with DAO, API, and LiveData
    public MovieRepository() {
        LocalDatabase db = LocalDatabase.getInstance(MyApplication.getAppContext());
        dao = db.movieDao();
        movieiListData = new MovieListData();
        api = new MovieApi(movieiListData, dao);
    }

    // Inner class that extends MutableLiveData to hold the list of movies
    class MovieListData extends MutableLiveData<List<Movie>> {
        public MovieListData () {
            super();
            setValue(new LinkedList<>());
        }
        // This method is triggered when the LiveData becomes active (e.g., observers are registered)
        @Override
        protected void onActive() {
            super.onActive();
            // Perform background operation to load movies from the database
            new Thread(() -> {
                List<Movie> movies = dao.index();
                Log.d("CategoryApi", "Categories loaded from DB: " + movies);
                postValue(movies);
            }).start();
        }
    }
    // Method to get the LiveData object that holds the list of movies
    public LiveData<List<Movie>> getAll() {
        return movieiListData;
    }

    // Method to add a new movie by calling the API and uploading files
    public void add (final Movie movie, File imageFile, File videoFile, File trailerFile) {
        api.add(movie,imageFile, videoFile, trailerFile);
    }

    // Method to edit an existing movie by calling the API and uploading files
    public void edit(String movieId,Movie movie, File imageFile, File videoFile, File trailerFile) {
        api.edit(movieId, movie, imageFile, videoFile, trailerFile);
    }
    // Method to reload the movie list by fetching data from the API
    public void reload () {
        api.getListOfMovies();
    }

    // Method to delete a movie by its ID from both the local database and the API
    public void deleteMovieById(Movie movie) {
        new Thread(() -> dao.delete(movie)).start();
        api.deleteMovie(movie.get_id());
    }
    // Method to get a movie by its ID, either from the local database or API if not found locally
    public LiveData<Movie> getMovieById(String id) {
        MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();

        // Perform the database query on a separate thread
        new Thread(() -> {
            Movie movie = dao.getMovieById(id);

            if (movie != null) {
                movieLiveData.postValue(movie);
            } else {
                // If the movie is not found in the local DB, call the API to fetch it
                api.getMovieById(id, new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            movieLiveData.postValue(response.body());
                        } else {
                            Log.d("MovieRepository", "API request failed");
                            movieLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.d("MovieRepository", "API failure");
                        movieLiveData.postValue(null); // או לשים ערך שגיאה
                    }
                });
            }
        }).start();
        // Return the LiveData containing the movie information
        return movieLiveData;
    }

}



