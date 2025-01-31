package com.example.androidapp.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.R;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.entities.PromotedCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi {
    private MutableLiveData<List<Movie>> movieListData;
    private MovieDao dao;
    Retrofit retrofit;
    ApiService apiService;

    public MovieApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public MovieApi(MutableLiveData<List<Movie>> movieListData, MovieDao dao) {
        this.movieListData = movieListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    String userId = "6792b52c10a40e0b80dd798d";

    public void getSearchedMovies(final Callback<List<Movie>> callback, String query) {
        Call<List<Movie>> call = apiService.getSearchedMovies(userId, query);

        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    callback.onFailure(call, new Throwable("Failed to get searched movies"));
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                callback.onFailure(call, t);
                }
            });
    }

//    public Movie getMovie(String movieId) {
//        Call<Movie> call = apiService.getMovie(userId, movieId);
//        call.enqueue(new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("MovieApi", "Received movie: " + response.body());
////                    if (response.body() != null && !response.body().isEmpty()) {
////                        for (PromotedCategory promotedCategory : response.body()) {
////                            Log.d("CategoryApi", "Category: " + promotedCategory.getCategoryName() + "," + promotedCategory.getMovies());
////                        }
////                    }
//                    new Thread(() -> {
//                        Log.d("MovieApi", "Thread started");
////                        dao.clear();
////                        dao.insertList(response.body());
////                        Log.d("CategoryApi", "Inserted categories into DB: " + response.body());
////                        movieListData.postValue(dao.index());
//                    }).start();
//                }
//                else {
//                    Log.d("MovieApi", "Failed to get movie: " + response.message());
//                }
//            }
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//                Log.e("MovieApi", "Error fetching movie: " + t.getMessage());
//            }
//        });
//    }
}
