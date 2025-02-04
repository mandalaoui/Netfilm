package com.example.androidapp.api;

import androidx.media3.common.C;

import com.example.androidapp.entities.Movie;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.entities.User;
import com.example.androidapp.entities.Category;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // Register a new user with the given details and profile picture
    @Multipart
    @POST("users")
    Call<User> post(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("nickname") RequestBody nickname,
            @Part MultipartBody.Part profilePicture
    );

    // Create a new movie with details like name, publication year, time, description, categories, age, and media files (image, video, trailer)
    @Multipart
    @POST("movies")
    Call<Movie> createMovie(
            @Header("userId") String userId,
            @Part("name") RequestBody name,
            @Part("Publication_year") RequestBody Publication_year,
            @Part("movie_time") RequestBody movie_time,
            @Part("description") RequestBody description,
            @Part("categories") RequestBody categories,
            @Part("age") RequestBody age,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video,
            @Part MultipartBody.Part trailer
            );

    @GET("users/{Id}")
    Call<User> getUser(@Path("Id") String Id, @Header("userId") String userId);

    // Handle user login by receiving a User object and returning a LoginResponse
    @POST("tokens")
    Call<LoginResponse> login(@Body User user);

    // Create a new category with the specified user ID and category details
    @POST("categories")
    Call<Category> createCategory(
            @Header("userId") String userId,
            @Body Category categoryRequest
    );

    // Edit an existing category by providing the category ID and updated details
    @PATCH("categories/{categoryId}")
    Call<Category> editCategory(
            @Header("userId") String userId,
            @Path("categoryId") String categoryId,
            @Body Category categoryRequest
    );

    // Retrieve a list of all categories for a user
    @GET("categories")
    Call<List<Category>> getAllCategories(@Header("userId") String userId);

    // Retrieve a list of all movies for a user
    @GET("movies/allmovies")
    Call<List<Movie>> getMovies(@Header("userId") String userId);

    // Search for movies based on a query
    @GET("movies/search/{query}")
    Call<List<Movie>> getSearchedMovies(@Header("userId") String userId, @Path("query") String query);

    // Delete a movie by its ID
    @DELETE("movies/{movieId}")
    Call<Movie> deleteMovie(@Path("movieId") String movieId, @Header("userId") String userId);

    // Retrieve the categories of movies for a user
    @GET("movies")
    Call<List<PromotedCategory>> getCategories(@Header("userId") String userId);

    // Add a movie to the user's watchlist
    @POST("movies/{id}/recommend")
    Call<User> addToWatchList(@Header("userId") String userId, @Path("id") String movieId);

    // Retrieve a list of recommended movies for a specific movie ID
    @GET("movies/{id}/recommend")
    Call<List<Movie>> getRecommendation (@Header("userId") String userId, @Path("id") String movieId);

    // Edit an existing movie by providing its ID and updated details, including media files
    @Multipart
    @PUT("movies/{movieId}")
    Call<Movie> editMovie(
            @Header("userId") String userId,
            @Path("movieId") String movieId,
            @Part("name") RequestBody name,
            @Part("Publication_year") RequestBody Publication_year,
            @Part("movie_time") RequestBody movie_time,
            @Part("description") RequestBody description,
            @Part("categories") RequestBody categories,
            @Part("age") RequestBody age,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video,
            @Part MultipartBody.Part trailer
    );

    // Delete a category by its ID
    @DELETE("categories/{categoryId}")
    Call<Category> deleteCategory(@Path("categoryId") String categoryId, @Header("userId") String userId);

    // Retrieve a movie by its ID for a specific user
    @GET("movies/{id}")
    Call<Movie> getMovieById (@Header("userId") String userId, @Path("id") String movieId);
}
