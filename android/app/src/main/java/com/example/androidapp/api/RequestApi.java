package com.example.androidapp.api;

import android.content.Context;
import android.util.Log;

import com.example.androidapp.AppContext;
import com.example.androidapp.Movie;
import com.example.androidapp.R;
import com.example.androidapp.entities.User;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestApi {
    private Retrofit retrofit;
    private ApiService apiService;
    private Context context;

    public RequestApi() {
        this.context = AppContext.getContext();
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public void registerUser(User user,File imageFile, final Callback<User> callback) {
        RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
        RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
        RequestBody nickname = RequestBody.create(user.getNickname(), MediaType.parse("text/plain"));
        Log.d("UserDetails", "Username: " + user.getUsername());
        Log.d("UserDetails", "Password: " + user.getPassword());
        Log.d("UserDetails", "Nickname: " + user.getNickname());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);

        Log.d("API_REQUEST", "Sending registration request for user: " + user.getUsername());

        Call<User> call = apiService.post(username, password, nickname, imagePart);
        call.enqueue(callback);
    }

    public void loginUser(User user, final Callback<ApiResponse> callback) {
        Log.d("API_REQUEST", "Sending registration request for user: 1" + user.getUsername());
        Call<ApiResponse> call = apiService.login(user);
        call.enqueue(callback);
    }

    public void getMovie(String movieId,String userId, final Callback<Movie> callback) {
        Call<Movie> call = apiService.getMovie(movieId, userId);
        call.enqueue(callback);

    }
    public void getRecommendMovie(String movieId,String userId, final Callback<List<Movie>> callback) {
        Call<List<Movie>> call = apiService.RecommendedMovies(movieId, userId);
        call.enqueue(callback);
    }






}
