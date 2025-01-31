package com.example.androidapp.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Multipart
    @POST("users")
    Call<User> post(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("nickname") RequestBody nickname,
            @Part MultipartBody.Part profilePicture
    );
    @Multipart
    @POST("movies")
    Call<Movie> createMovie(
            @Header("userId") String userId,
            @Part("name") RequestBody name,
            @Part("Publication_year") RequestBody Publication_year,
            @Part("movie_time") RequestBody movie_time,
            @Part("description") RequestBody description,
            @Part("categories") RequestBody categories,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video
    );
    @GET("movies/{movieId}")
    Call<Movie> getMovie(@Path("movieId") String movieId, @Header("userId") String userId);
    @GET("movies/{movieId}/recommend/")
    Call<List<Movie>> RecommendedMovies(@Path("movieId") String movieId, @Header("userId") String userId);
    @POST("tokens")
    Call<LoginResponse> login(@Body User user);

//    @POST("categories/")
//    Call<Void> createCategory(
//            @Header("userId") String userId,       // שולחים את ה- userId כ- header
//            @Body Category categoryRequest  // שולחים את הנתונים כ- body
//    );
//    @GET("movies")
//    Call<List<Category>> getCategories(@Header("userId") String userId);

    @GET("categories")
    Call<List<Category>> getAllCategories(@Header("userId") String userId);
    @GET("movies/allmovies")
    Call<List<Movie>> getMovies(@Header("userId") String userId);
    @GET("movies/search/{query}")
    Call<List<Movie>> getSearchedMovies(@Header("userId") String userId, @Path("query") String query);

    @DELETE("movies/{movieId}")
        Call<Movie> deleteMovie(@Path("movieId") String movieId, @Header("userId") String userId);


}
