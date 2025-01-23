package com.example.androidapp.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.androidapp.AppContext;
import com.example.androidapp.ImageResponse;
import com.example.androidapp.R;
import com.example.androidapp.User;
import com.example.androidapp.RegisterActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private Retrofit retrofit;
    private ApiService apiService;
    private Context context;

    public UserApi() {
//        this.context = context;

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public void registerUser(User user, final Callback<User> callback) {

        Log.d("API_REQUEST", "Sending registration request for user: " + user.getUsername());

        Call<User> call = apiService.post(user);
        call.enqueue(callback);
    }

    public void loginUser(User user, final Callback<ApiResponse> callback) {
        Log.d("API_REQUEST", "Sending registration request for user: 1" + user.getUsername());
        Call<ApiResponse> call = apiService.login(user);
        call.enqueue(callback);
    }

    public void uploadImage(MultipartBody.Part imagePart, final Callback<ImageResponse> callback) {
        Log.d("API_REQUEST", "Sending registration request for user: 1" + imagePart);
        Call<ImageResponse> call = apiService.uploadImage(imagePart);
        call.enqueue(callback);
    }
}
