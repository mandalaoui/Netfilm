package com.example.androidapp.api;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.R;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.entities.Movie;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public void getListOfMovies() {
        String userId = "679178e884e6da9a833f5452";
        Call<List<Movie>> call = apiService.getMovies(userId);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        Log.d("MovieApi", "Thread started");
                        dao.clear();
                        dao.insertList(response.body());
                        Log.d("CategoryApi", "Inserted categories into DB: " + response.body());
                        movieListData.postValue(dao.index());
                    }).start();

                } else {
                    Log.e("Movie", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("CategoryApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }


    public void add(Movie movieCreate, File imageFile, File videoFile) {
        RequestBody categoriesRequestBody;
        List<String> categories = movieCreate.getCategories();
        Log.d("Categories", categories.toString());

        categoriesRequestBody = RequestBody.create(
                TextUtils.join(",", categories), MediaType.parse("text/plain")  // שליחה כ-text/plain, ברשימה מופרדת בפסיקים
        );

        RequestBody name = RequestBody.create(movieCreate.getName(),MediaType.parse("text/plain"));
        RequestBody year = RequestBody.create(String.valueOf(movieCreate.getPublication_year()),MediaType.parse("text/plain"));
        RequestBody time = RequestBody.create(movieCreate.getMovie_time(),MediaType.parse("text/plain") );
        RequestBody description = RequestBody.create(movieCreate.getDescription(),MediaType.parse("text/plain"));

        RequestBody requestFileImage = RequestBody.create(imageFile,MediaType.parse("image/*"));
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFileImage);

        RequestBody requestFileMovie = RequestBody.create(videoFile,MediaType.parse("video/*"));
        MultipartBody.Part video = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFileMovie);

        String userId= "679213ef1cebc10d8c2d7bc3";
        Call<Movie> call = apiService.createMovie(userId,name, year, time, description,categoriesRequestBody, image, video);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insertMovie(response.body());
                    }).start();

                    Toast.makeText(AppContext.getContext(), "Movie created successfully", Toast.LENGTH_SHORT).show();
                    Log.d("RequestApi", "Movie created successfully");
                } else {
                    Log.e("RequestApi", "Failed to create movie: " + response.message());
                    try {
                        String errorResponse = response.errorBody().string();  // תקבל את התגובה השגויה כאן
                        Log.e("Error Response", errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("RequestApi", "Error: " + t.getMessage());
                Toast.makeText(AppContext.getContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteMovie(String movieId) {
        String userId = "679178e884e6da9a833f5452";
        Call<Movie> call = apiService.deleteMovie(userId,movieId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {

                } else {
                    Log.e("Movie", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MovieApi", "Error delete movie: " + t.getMessage());
            }
        });
    }
}
