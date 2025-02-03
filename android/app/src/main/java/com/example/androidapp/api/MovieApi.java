package com.example.androidapp.api;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;


import com.example.androidapp.MyApplication;
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

    // Constructor to initialize Retrofit and ApiService
    public MovieApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    // Constructor with additional parameters for handling movie data and database
    public MovieApi(MutableLiveData<List<Movie>> movieListData, MovieDao dao) {
        this.movieListData = movieListData;
        this.dao = dao;
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    MyApplication myApplication = MyApplication.getInstance();

    String userId = myApplication.getGlobalUserId();

    // Method to search for movies based on a query string
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

    // Method to get a list of all movies
    public void getListOfMovies() {

        Call<List<Movie>> call = apiService.getMovies(userId);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        Log.d("MovieApi", "Thread started");
                        dao.clear();
                        dao.insertList(response.body());
                        Log.d("MovieApi", "Inserted categories into DB: " + response.body());
                        movieListData.postValue(dao.index());
                    }).start();

                } else {
                    Toast.makeText(MyApplication.getAppContext(), response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("MovieApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }

    // Method to add a new movie along with its image, video, and trailer
    public void add(Movie movieCreate, File imageFile, File videoFile, File trailerFile) {
        RequestBody categoriesRequestBody;
        List<String> categories = movieCreate.getCategories();

        categoriesRequestBody = RequestBody.create(
                TextUtils.join(",", categories), MediaType.parse("text/plain")  // שליחה כ-text/plain, ברשימה מופרדת בפסיקים
        );
        // Prepare other fields for the movie as request bodies
        RequestBody name = RequestBody.create(movieCreate.getName(),MediaType.parse("text/plain"));
        RequestBody year = RequestBody.create(String.valueOf(movieCreate.getPublication_year()),MediaType.parse("text/plain"));
        RequestBody time = RequestBody.create(movieCreate.getMovie_time(),MediaType.parse("text/plain") );
        RequestBody description = RequestBody.create(movieCreate.getDescription(),MediaType.parse("text/plain"));
        RequestBody age = RequestBody.create(String.valueOf(movieCreate.getAge()),MediaType.parse("text/plain"));


        RequestBody requestFileImage = RequestBody.create(imageFile, MediaType.parse("image/*"));
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFileImage);

        RequestBody requestFileMovie = RequestBody.create(videoFile, MediaType.parse("video/*"));
        MultipartBody.Part video = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFileMovie);

        RequestBody requestFileTrailer = RequestBody.create(trailerFile,MediaType.parse("video/*"));
        MultipartBody.Part trailer = MultipartBody.Part.createFormData("trailer", trailerFile.getName(), requestFileTrailer);

        // Make the API call to create a movie
        Call<Movie> call = apiService.createMovie(userId,name, year, time, description,categoriesRequestBody, age, image, video, trailer);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insertMovie(response.body());
                    }).start();
                    Log.d("MovieApi", "Movie created successfully");
                } else {
                    Toast.makeText(MyApplication.getAppContext(), response.message(), Toast.LENGTH_SHORT).show();
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
                Log.e("MovieApi", "Error: " + t.getMessage());
            }
        });
    }

    // Method to delete a movie by its ID
    public void deleteMovie(String movieId) {

        Call<Movie> call = apiService.deleteMovie(movieId, userId);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Log.d("MovieApi", "Movie delete successfully");
                } else {
                    Toast.makeText(MyApplication.getAppContext(), response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("MovieApi", "Error delete movie: " + t.getMessage());
            }
        });

    }
    // Method to recommend a movie based on a given movie ID
    public void recommend(String movieId, final Callback<List<Movie>> callback) {
        Call<List<Movie>> call = apiService.getRecommendation(userId, movieId);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    if (response.code() == 404) {
                        callback.onFailure(call, new Throwable("Failed to get searched movies"));
                    } else {
                        callback.onFailure(call, new Throwable("Failed to get searched movies"));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
    // Method to edit an existing movie
    public void edit(String movieId, Movie movieEdit, File imageFile, File videoFile, File trailerFile) {
        RequestBody categoriesRequestBody;
        List<String> categories = movieEdit.getCategories();

        categoriesRequestBody = RequestBody.create(
                TextUtils.join(",", categories), MediaType.parse("text/plain")  // שליחה כ-text/plain, ברשימה מופרדת בפסיקים
        );
        // Prepare other fields for the movie as request bodies
        RequestBody name = RequestBody.create(movieEdit.getName(),MediaType.parse("text/plain"));
        RequestBody year = RequestBody.create(String.valueOf(movieEdit.getPublication_year()),MediaType.parse("text/plain"));
        RequestBody time = RequestBody.create(movieEdit.getMovie_time(),MediaType.parse("text/plain") );
        RequestBody description = RequestBody.create(movieEdit.getDescription(),MediaType.parse("text/plain"));
        RequestBody age = RequestBody.create(String.valueOf(movieEdit.getAge()),MediaType.parse("text/plain"));

        RequestBody requestFileImage = RequestBody.create(imageFile,MediaType.parse("image/*"));
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFileImage);

        RequestBody requestFileMovie = RequestBody.create(videoFile,MediaType.parse("video/*"));
        MultipartBody.Part video = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFileMovie);

        RequestBody requestFileTrailer = RequestBody.create(trailerFile,MediaType.parse("video/*"));
        MultipartBody.Part trailer = MultipartBody.Part.createFormData("trailer", trailerFile.getName(), requestFileTrailer);

        // Making the network request to edit the movie
        Call<Movie> call = apiService.editMovie(userId,movieId,name, year, time, description,categoriesRequestBody,age, image, video, trailer);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.update(response.body());
                    }).start();
                } else {
                    Toast.makeText(MyApplication.getAppContext(), response.message(), Toast.LENGTH_SHORT).show();
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
                Log.e("MovieApi", "Error: " + t.getMessage());
            }
        });
    }
    // Function to get a movie by its ID
    public void getMovieById(String id, final Callback<Movie> callback) {
        Call<Movie> call = apiService.getMovieById(userId, id);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    // אם התגובה הצליחה, מחזירים את הרשימה דרך ה-Callback
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    if (response.code() == 404) {
                        callback.onFailure(call, new Throwable("Failed to get movies"));
                    } else {
                        callback.onFailure(call, new Throwable("Failed to get movies"));
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }
}
