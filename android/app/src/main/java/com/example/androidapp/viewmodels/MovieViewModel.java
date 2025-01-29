package com.example.androidapp.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Movie;
import com.example.androidapp.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private LiveData<List<Movie>> moviesFromApi;

    public MovieViewModel(Application application) {
        super(application);
        repository = new MovieRepository(application);
        moviesFromApi = repository.getMoviesFromApi();
    }

    public LiveData<List<Movie>> getMoviesFromApi() {
        return moviesFromApi;
    }

    public void fetchMoviesFromApi() {
        repository.fetchMoviesFromApi();
    }


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
