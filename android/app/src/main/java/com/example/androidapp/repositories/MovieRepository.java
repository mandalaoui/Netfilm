package com.example.androidapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.MovieDao;
import com.example.androidapp.MovieDatabase;
import com.example.androidapp.entities.Movie;

public class MovieRepository {
    private MovieDao dao;
    private MutableLiveData<Movie> selectedVideoItem = new MutableLiveData<>();

    public MovieRepository() {
        MovieDatabase db = MovieDatabase.getInstance(AppContext.getContext());
        dao = db.movieDao();
//        movieLiveData = new MutableLiveData<>();
    }

    public LiveData<Movie> getMovie() {
        return selectedVideoItem;
    }

    public void setSelectedMovie(Movie movie) {
        selectedVideoItem.setValue(movie);
    }

    // Method to fetch a single movie by its ID and return it
//    public LiveData<Movie> getMovie(String movieId, String userId) {
//        // קריאת ה- API דרך RequestApi
//        RequestApi requestApi = new RequestApi();
//        requestApi.getMovie(movieId, userId, new Callback<Movie>() {
//            @Override
//            public void onResponse(Call<Movie> call, Response<Movie> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Movie movie = response.body();
//
//                    // Insert the movie into the database
//                    new Thread(() -> {
//                        movieLiveData.postValue(movie);  // עדכון ה-LiveData עם הסרט
//                    }).start();
//                } else {
//                    Log.e("MovieRepository", "Error fetching movie: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Movie> call, Throwable t) {
//                Log.e("MovieRepository", "Failed to fetch movie: " + t.getMessage());
//            }
//        });
//
//        // מחזיר את ה-LiveData עם הסרט
//        return movieLiveData;
//    }
}


//public class MovieRepository {
//
//    private RequestApi requestApi;
//
//    public MovieRepository() {
//        requestApi = new RequestApi();
//    }
//
//    // בקשה לקבלת סרט לפי ID
//    public void getMovie(String movieId, String userId, Callback<Movie> callback) {
//        requestApi.getMovie(movieId, userId, callback);
//    }
//}
