package com.example.androidapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Movie;
import com.example.androidapp.repositories.MovieRepository;

public class MovieViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private LiveData<Movie> movieLiveData;

    public MovieViewModel() {
        movieRepository = new MovieRepository();
        movieLiveData = movieRepository.getMovie();
    }

    public void setSelectedMovie(Movie movie){
        movieRepository.setSelectedMovie(movie);
    }

    public LiveData<Movie> getMovie() {
        return movieLiveData;

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
