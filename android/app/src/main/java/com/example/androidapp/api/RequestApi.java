package com.example.androidapp.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.androidapp.AppContext;
import com.example.androidapp.Movie;
import com.example.androidapp.R;
import com.example.androidapp.activity.ManagmentActivity;
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

    public void createMovie(Movie movie, File imageFile, File videoFile) {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), movie.getName());
        RequestBody year = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(movie.getPublication_year()));
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), movie.getMovie_time());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), movie.getDescription());

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        requestFile = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), requestFile);
        Log.d("VideoDetails", "name: " + name);
        Log.d("VideoDetails", "year: " +year);
        Log.d("VideoDetails", "time: " + time);

        String userID = "679213ef1cebc10d8c2d7bc3";
        Call<Movie> call = apiService.createMovie(userID,name, year, time, description, imagePart, videoPart);
        call.enqueue(new Callback<Movie>() {
            public void onResponse(Call<Movie> call, retrofit2.Response<Movie> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Movie created successfully", Toast.LENGTH_SHORT).show();
                    Log.d("RequestApi", "Movie created successfully");
                } else {
                    String errorMessage = "Failed to create movie: " + response.message();
                    Log.e("RequestApi", "Failed to create movie: " + response.message());
                    Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("RequestApi", "Error: " + t.getMessage());
                Toast.makeText(context, "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
