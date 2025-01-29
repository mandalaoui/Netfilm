package com.example.androidapp.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.Category;
import com.example.androidapp.MovieDao;
import com.example.androidapp.MovieDatabase;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.entities.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private RequestApi requestApi;
    private MutableLiveData<List<Movie>> moviesFromApi = new MutableLiveData<>();

    public MovieRepository(Application application) {
        requestApi = new RequestApi(application);
    }

    public LiveData<List<Movie>> getMoviesFromApi() {
        return moviesFromApi;
    }

    public void fetchMoviesFromApi() {
        List<Movie> movieList = requestApi.createCategory();
        moviesFromApi.setValue(movieList);

//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Category> categories = response.body();
//
//
////                    Log.e("MovieViewModel",movies.toString());
//                    moviesFromApi.setValue(uniqueMoviesList);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Log.e("MovieViewModel", "Failed to fetch movies", t);
//            }
//        });
    }
}



