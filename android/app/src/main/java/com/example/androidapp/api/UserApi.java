package com.example.androidapp.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.entities.Category;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.R;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.entities.User;

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
//    private Retrofit retrofit;
//    private ApiService apiService;
//
//    public RequestApi() {
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                .build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        apiService = retrofit.create(ApiService.class);
//    }
//    public void registerUser(User user, final Callback<User> callback) {
//
//        Log.d("API_REQUEST", "Sending registration request for user: " + user.getUsername());
//
//        Call<User> call = apiService.post(user);
//        call.enqueue(callback);
//    }
//
//    public void loginUser(User user, final Callback<ApiResponse> callback) {
//        Log.d("API_REQUEST", "Sending registration request for user: 1" + user.getUsername());
//        Call<ApiResponse> call = apiService.login(user);
//        call.enqueue(callback);
//    }


    private Retrofit retrofit;
    private ApiService apiService;
    private Context context;
    private RequestBody categoriesRequestBody;
    private MovieDao movieDao;
    private MutableLiveData<List<Movie>> movieListData;
    public UserApi(Context context) {
        this.context = context;
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public void registerUser(User user,File imageFile) {
        RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
        RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
        RequestBody nickname = RequestBody.create(user.getNickname(), MediaType.parse("text/plain"));
        Log.d("UserDetails", "Username: " + user.getUsername());
        Log.d("UserDetails", "Password: " + user.getPassword());
        Log.d("UserDetails", "Nickname: " + user.getNickname());

        RequestBody requestFile = RequestBody.create(imageFile,MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);

        Log.d("API_REQUEST", "Sending registration request for user: " + user.getUsername());

        Call<User> call = apiService.post(username, password, nickname, imagePart);
        Log.d("Register", "Starting registration request for user: " + user.getUsername());
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d("Register", "Response raw body: " + response.raw().body());

                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        Log.d("API_RESPONSE", "User successfully created!");
                        Toast.makeText(context, "User created successfully!", Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(context, HomeActivity.class);
//                        i.putExtra("movieId", "679629522d6eaf038e9e1768");
//                        startActivity(context, i, null);
                    } else {
                        if (response.body() != null) {
                            Log.d("API_RESPONSE", "User created: " + response.body());
                            Toast.makeText(context, "User created: " + response.body().getUsername(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {

                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();  // לקרוא את הגוף של השגיאה
                            Log.e("Register", "Error: " + errorMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Register", "An error occurred");
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Register", "Registration failed with error: " + t.getMessage());
                // Logging the error
                t.printStackTrace();
                Toast.makeText(context, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(RegisterActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loginUser(User user, final Callback<LoginResponse> callback) {
        Log.d("API_REQUEST", "Sending registration request for user: 1" + user.getUsername());
        Call<LoginResponse> call = apiService.login(user);
        call.enqueue(callback);
    }


    public void getRecommendMovie(String movieId,String userId, final Callback<List<Movie>> callback) {
        Call<List<Movie>> call = apiService.RecommendedMovies(movieId, userId);
        call.enqueue(callback);
    }

    public void getCategories(final Callback<List<Category>> callback) {
        String userid = "679178e884e6da9a833f5452";
        Call<List<Category>> call = apiService.getAllCategories(userid);

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, Response.success(response.body()));
                } else {
                    callback.onFailure(call, new Throwable("Failed to get categories"));
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }

}

