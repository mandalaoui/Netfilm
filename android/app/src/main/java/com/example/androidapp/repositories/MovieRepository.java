package com.example.androidapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.dao.MovieDao;
import com.example.androidapp.entities.LocalDatabase;
import com.example.androidapp.entities.Movie;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
                List<Movie> movies = dao.index();
                Log.d("CategoryApi", "Categories loaded from DB: " + movies);
                postValue(movies);
            }).start();
        }
    }

    public LiveData<List<Movie>> getAll() {
        return movieiListData;
    }

    public void add (final Movie movie, File imageFile, File videoFile, File trailerFile) {
        api.add(movie,imageFile, videoFile, trailerFile);
    }

    public void edit(String movieId,Movie movie, File imageFile, File videoFile, File trailerFile) {
        api.edit(movieId, movie, imageFile, videoFile, trailerFile);
    }

    public void reload () {
        api.getListOfMovies();
    }

    public void deleteMovieById(Movie movie) {
        new Thread(() -> dao.delete(movie)).start();
        api.deleteMovie(movie.get_id());
    }
}



