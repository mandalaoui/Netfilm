package com.example.androidapp.api;

import android.content.Context;
import android.text.TextUtils;
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

public class RequestApi {
    private Retrofit retrofit;
    private ApiService apiService;
    private Context context;
    private RequestBody categoriesRequestBody;
    private MovieDao movieDao;
    private MutableLiveData<List<Movie>> movieListData;
//    private MovieDatabase MovieDatabase;
//    private List<Movie> allMoviesList = new ArrayList<>();
//    private List<Category> categories = new ArrayList<>();

    public RequestApi(Context context) {
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
//    public void createMovie(Movie movieCreate, File imageFile, File videoFile) {
//
//        List<String> categories = movieCreate.getCategories();
//        Log.d("Categories", categories.toString());
//
//         categoriesRequestBody = RequestBody.create(
//                TextUtils.join(",", categories), MediaType.parse("text/plain")  // שליחה כ-text/plain, ברשימה מופרדת בפסיקים
//        );
//
//        RequestBody name = RequestBody.create(movieCreate.getName(),MediaType.parse("text/plain"));
//        RequestBody year = RequestBody.create(String.valueOf(movieCreate.getPublication_year()),MediaType.parse("text/plain"));
//        RequestBody time = RequestBody.create(movieCreate.getMovie_time(),MediaType.parse("text/plain") );
//        RequestBody description = RequestBody.create(movieCreate.getDescription(),MediaType.parse("text/plain"));
//
//        RequestBody requestFileImage = RequestBody.create(imageFile,MediaType.parse("image/*"));
//        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFileImage);
//
//        RequestBody requestFileMovie = RequestBody.create(videoFile,MediaType.parse("video/*"));
//        MultipartBody.Part video = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFileMovie);
//
//        String userId= "679213ef1cebc10d8c2d7bc3";
//        Call<Movie> call = apiService.createMovie(userId,name, year, time, description,categoriesRequestBody, image, video);
//        call.enqueue(new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(context, "Movie created successfully", Toast.LENGTH_SHORT).show();
//                    Log.d("RequestApi", "Movie created successfully");
//                } else {
//                    Log.e("RequestApi", "Failed to create movie: " + response.message());
//                    try {
//                        String errorResponse = response.errorBody().string();  // תקבל את התגובה השגויה כאן
//                        Log.e("Error Response", errorResponse);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//                Log.e("RequestApi", "Error: " + t.getMessage());
//                Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    public void getListOfMovies( final Callback<List<Movie>> callback) {
//        String userId = "679178e884e6da9a833f5452";
//        Call<List<Movie>> call = apiService.getMovies(userId);
//        call.enqueue(new Callback<List<Movie>>() {
//            @Override
//            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
//                if (response.isSuccessful()) {
//                    List<Movie> movie = response.body();
//                    callback.onResponse(call, Response.success(movie));
//                } else {
//                    Log.e("Movie", "Error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Movie>> call, Throwable t) {
//                callback.onFailure(call, t);
//
//            }
//        });
//    }
}
