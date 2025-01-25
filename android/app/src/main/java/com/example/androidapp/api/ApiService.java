package com.example.androidapp.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

import com.example.androidapp.ImageResponse;
import com.example.androidapp.Movie;
import com.example.androidapp.User;

public interface ApiService {

    @Multipart
    @POST("users")
    Call<User> post(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("nickname") RequestBody nickname,
            @Part MultipartBody.Part profilePicture
    );

    @GET("movies/{movieId}")
    Call<Movie> getMovie(@Path("movieId") String movieId, @Header("userId") String userId);
    @POST("tokens")
    Call<ApiResponse> login(@Body User user);
}
