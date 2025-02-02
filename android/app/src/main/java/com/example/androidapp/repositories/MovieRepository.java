package com.example.androidapp.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//import com.example.androidapp.AppContext;
import com.example.androidapp.MyApplication;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.entities.LocalDatabase;
import com.example.androidapp.entities.Movie;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private MovieDao dao;
    private MovieListData movieiListData;
    private MovieApi api;
    public MovieRepository() {
        LocalDatabase db = LocalDatabase.getInstance(MyApplication.getAppContext());
        dao = db.movieDao();
        movieiListData = new MovieListData();
        api = new MovieApi(movieiListData, dao);
    }

    class MovieListData extends MutableLiveData<List<Movie>> {
        public MovieListData () {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
//                categoryListData.postValue(dao.index());
                List<Movie> movies = dao.index();
                Log.d("CategoryApi", "Categories loaded from DB: " + movies);
                postValue(movies);
            }).start();
        }
    }

    public LiveData<List<Movie>> getAll() {
        return movieiListData;
    }

    public void add (final Movie movie, File imageFile, File videoFile) {
        api.add(movie,imageFile, videoFile);
    }
//
//    public void delete (final Category category) {
//        api.delete(category);
//    }

    public void reload () {
//        api.reload();
        api.getListOfMovies();
    }

    public void deleteMovieById(String movieId) {
//        LocalDatabase.databaseWriteExecutor.execute(() -> {
        new Thread(() -> dao.deleteMovieById(movieId)).start();
//        });
        api.deleteMovie(movieId);
    }


//    public LiveData<Movie> getMovieById(String id) {
//        MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();
//        Movie movie = dao.getMovieById(id);
//        if (movie != null)
//            movieLiveData.setValue(movie);
//        else {
//            api.getMovieById(id, new Callback<Movie>() {
//                @Override
//                public void onResponse(Call<Movie> call, Response<Movie> response) {
//                    if (response.isSuccessful()) {
//                        movieLiveData.setValue(response.body());
//                    } else {
//                        Log.d("MovieRepository", "fail");
//                        movieLiveData.setValue(null); // או תוכל לשים ערך שגיאה פה
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Movie> call, Throwable t) {
//                    Log.d("MovieRepository", "failure");
//                    movieLiveData.setValue(null); // או תוכל לשים ערך שגיאה פה
//                }
//            });
//        }
//        return movieLiveData;
//    }

    public LiveData<Movie> getMovieById(String id) {
        MutableLiveData<Movie> movieLiveData = new MutableLiveData<>();

        // נבצע את הקריאה למסד הנתונים על Thread נפרד
        new Thread(() -> {
            Movie movie = dao.getMovieById(id); // קריאה למסד הנתונים

            if (movie != null) {
                movieLiveData.postValue(movie); // אם נמצא, מעדכנים ב-LiveData
            } else {
                // אם הסרט לא נמצא במסד, נבצע קריאה ל-API
                api.getMovieById(id, new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            movieLiveData.postValue(response.body()); // עדכון עם הסרט שהתקבל
                        } else {
                            Log.d("MovieRepository", "API request failed");
                            movieLiveData.postValue(null); // או לשים ערך שגיאה
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        Log.d("MovieRepository", "API failure");
                        movieLiveData.postValue(null); // או לשים ערך שגיאה
                    }
                });
            }
        }).start();

        return movieLiveData;
    }

}



