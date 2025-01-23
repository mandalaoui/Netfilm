package com.example.androidapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.androidapp.User;

public interface ApiService {
    @POST("users")
    Call<User> post(@Body User user);

    @POST("tokens")
    Call<ApiResponse> login(@Body User user);
}
