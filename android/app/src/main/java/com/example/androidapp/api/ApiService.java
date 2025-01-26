package com.example.androidapp.api;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users")
    Call<User> post(@Body User user);

    @POST("tokens")
    Call<ApiResponse> login(@Body User user);

    @GET("movies")
    Call<List<Category>> getCategories(@Header("userId") String userId);
}
