package com.example.androidapp.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import com.example.androidapp.ImageResponse;
import com.example.androidapp.User;

public interface ApiService {
    @POST("users")
    Call<User> post(@Body User user);

    @Multipart
    @POST("upload")  // endpoint של השרת שלך להעלאת תמונה
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part image);
    @POST("tokens")
    Call<ApiResponse> login(@Body User user);
}
