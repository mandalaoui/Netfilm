package com.example.androidapp.api;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

//import com.example.androidapp.AppContext;
import com.example.androidapp.MyApplication;
import com.example.androidapp.activities.HomeActivity;
import com.example.androidapp.activities.LoginActivity;
import com.example.androidapp.activities.RegisterActivity;
import com.example.androidapp.entities.Category;
import com.example.androidapp.dao.MovieDao;
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
    private RequestBody categoriesRequestBody;
    private MovieDao movieDao;
    private MutableLiveData<List<Movie>> movieListData;
//    private MyApplication myApplication;
    public UserApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    MyApplication myApplication = MyApplication.getInstance();

    String userId = myApplication.getGlobalUserId();
    public void registerUser(User user,File imageFile) {
        RequestBody username = RequestBody.create(user.getUsername(), MediaType.parse("text/plain"));
        RequestBody password = RequestBody.create(user.getPassword(), MediaType.parse("text/plain"));
        RequestBody nickname = RequestBody.create(user.getNickname(), MediaType.parse("text/plain"));
        Log.d("UserDetails", "Username: " + user.getUsername());
        Log.d("UserDetails", "Password: " + user.getPassword());
        Log.d("UserDetails", "Nickname: " + user.getNickname());
        RequestBody requestFile = RequestBody.create(imageFile,MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("photo", imageFile.getName(), requestFile);

        Log.d("UserApi", "Sending registration request for user: " + user.getUsername());

        Call<User> call = apiService.post(username, password, nickname, imagePart);
        Log.d("UserApi", "Starting registration request for user: " + user.getUsername());
        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d("UserApi", "Response raw body: " + response.raw().body());

                if (response.isSuccessful()) {
                    if (response.code() == 201) {
                        Log.d("UserApi", "User successfully created!");
                        Toast.makeText(MyApplication.getAppContext(), "User created successfully!", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MyApplication.getAppContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
                    } else {
                        if (response.body() != null) {
                            Log.d("UserApi", "User created: " + response.body());
                            Toast.makeText(MyApplication.getAppContext(), "User created: " + response.body().getUsername(), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {

                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();  // לקרוא את הגוף של השגיאה
                            Log.e("UserApi", "Error: " + errorMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("UserApi", "An error occurred");
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserApi", "Registration failed with error: " + t.getMessage());
                // Logging the error
                t.printStackTrace();
                Toast.makeText(MyApplication.getAppContext(), "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                            Toast.makeText(RegisterActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loginUser(User user) {
        Log.d("UserApi", "Sending registration request for user: 1" + user.getUsername());
        Call<LoginResponse> call = apiService.login(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("UserApi", "Response raw body: " + response.raw().body());
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getToken() != null) {
//                        String userId = loginResponse.getUserId();
//                        token.setGlobalToken(userId);
                        extractDataFromToken(loginResponse.getToken());
                        Log.d("UserApi", "User successfully logged i ");

                        Toast.makeText(MyApplication.getAppContext(), "User successfully logged in!", Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(MyApplication.getAppContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MyApplication.getAppContext().startActivity(i);
//                        startActivity(MyApplication.getAppContext(),i,null);
                    }
                } else if (response.code() == 404) {
                    Log.d("UserApi", "The user is not in the system ");

                    Toast.makeText(MyApplication.getAppContext(), "The user is not in the system, please check the login information", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("UserApi", "Login error");

                    Toast.makeText(MyApplication.getAppContext(), "Login error, please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Error connecting to the server", t);

                Toast.makeText(MyApplication.getAppContext(), "Error connecting to the server, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

//        call.enqueue(callback);

//    public void getRecommendMovie(String movieId,String userId, final Callback<List<Movie>> callback) {
//        Call<List<Movie>> call = apiService.RecommendedMovies(movieId, userId);
//        call.enqueue(callback);
//    }
    public void getCategories(final Callback<List<Category>> callback) {
        String userid = "6792b52c10a40e0b80dd798d";
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

    public void addToWatchList(String movieId) {
        Log.d("API_REQUEST", "userId" + movieId + "userElse");
        Call<User> call = apiService.addToWatchList(userId,movieId);
        call.enqueue(new Callback<User>() {
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d("UserApi", "Response raw body: " + response.raw().body());

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

    public void extractDataFromToken(String token) {
        try {
            // 1. JWT מבנה: header.payload.signature
            String[] parts = token.split("\\.");

            // 2. הפונקציה מפענחת את ה-payload (החלק השני בטוקן)
            String payload = parts[1];

            // 3. דה-קידוד של ה-payload מ-Base64
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));

            // 4. המרת ה-payload ל-JSONObject
            JSONObject jsonPayload = new JSONObject(decodedPayload);

            // 5. חילוץ ה-id וה-isAdmin
            MyApplication myApplication = MyApplication.getInstance();
            myApplication.setGlobalUserId(jsonPayload.getString("id"));
            myApplication.setAdmin(jsonPayload.getBoolean("isAdmin"));
            String userId = jsonPayload.getString("id");
            boolean isAdmin = jsonPayload.getBoolean("isAdmin");

            // הדפסת הערכים
            System.out.println("User ID: " + userId);
            System.out.println("isAdmin: " + isAdmin);

            // כאן אתה יכול להחזיר את המידע או לשמור אותו לשימוש מאוחר יותר
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

