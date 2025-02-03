package com.example.androidapp.api;

import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.activities.HomeActivity;
import com.example.androidapp.activities.LoginActivity;
import com.example.androidapp.entities.Category;
import com.example.androidapp.R;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.entities.User;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private Retrofit retrofit;
    private ApiService apiService;

    // Constructor to initialize Retrofit client with logging interceptor
    public UserApi() {
        // Set up Retrofit with Gson converter and base URL from resources
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    MyApplication myApplication = MyApplication.getInstance();

    String userId = myApplication.getGlobalUserId();

    // Register a new user with username, password, nickname, and profile image
    public void registerUser(User user,File imageFile) {
        // Create RequestBody for the user's data and the image
        RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
        RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
        RequestBody nickname = RequestBody.create(user.getNickname(), MediaType.parse("text/plain"));

        // Handle the image file as MultipartBody
        RequestBody requestFile = RequestBody.create(imageFile,MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);

        // Make the API call to register the user
        Call<User> call = apiService.post(username, password, nickname, imagePart);
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // On successful registration, navigate to the LoginActivity
                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        Intent i = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(MyApplication.getAppContext(), response.body().getUsername(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    // Handle errors during registration
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();  // לקרוא את הגוף של השגיאה
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserApi", "Registration failed with error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // Log in the user with provided credentials
    public void loginUser(User user) {
        // Make the API call to log in the user
        Call<LoginResponse> call = apiService.login(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getToken() != null) {
                        extractDataFromToken(loginResponse.getToken());

                        // Notify the user of successful login and navigate to HomeActivity
                        Intent i = new Intent(MyApplication.getAppContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
                    }
                } else if (response.code() == 404) {
                    // Handle the case where the user is not found
                    Toast.makeText(MyApplication.getAppContext(), "The user is not in the system, please check the login information", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle general login error
                    Toast.makeText(MyApplication.getAppContext(), "Login error, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Error connecting to the server", t);
            }
        });
    }

    // Add a movie to the user's watchlist
    public void addToWatchList(String movieId) {
        Call<User> call = apiService.addToWatchList(userId,movieId);
        call.enqueue(new Callback<User>() {
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d("UserApi", "add movie to watchList: " + response.body());
                }  else {
                    Log.d("UserApi", "Failed add movie to watchList" + response.body());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("UserApi", "Failed add movie to watchList 1" + t);
            }
        });

    }

    // Extract user-specific data (like user ID and admin status) from JWT token
    public void extractDataFromToken(String token) {
        try {
            // Decode the JWT token (header, payload, signature)
            String[] parts = token.split("\\.");
            String payload = parts[1];

            // Decode the payload from Base64
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));

            // Convert the payload into a JSON object
            JSONObject jsonPayload = new JSONObject(decodedPayload);

            // Extract user ID and admin status
            MyApplication myApplication = MyApplication.getInstance();
            myApplication.setGlobalUserId(jsonPayload.getString("id"));
            myApplication.setAdmin(jsonPayload.getBoolean("isAdmin"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

