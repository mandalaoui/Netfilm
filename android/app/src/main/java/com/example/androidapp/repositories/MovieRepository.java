//package com.example.androidapp.repositories;
//
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.androidapp.AppContext;
//import com.example.androidapp.api.MovieApi;
//import com.example.androidapp.entities.Category;
//import com.example.androidapp.entities.LocalDatabase;
//import com.example.androidapp.entities.Movie;
//import com.example.androidapp.dao.MovieDao;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class MovieRepository {
//    private MovieDao dao;
//    private MovieRepository.MovieListData movieListData;
//    private MovieApi api;
//
//    public MovieRepository() {
//        LocalDatabase db = LocalDatabase.getInstance(AppContext.getContext());
//        dao = db.movieDao();
//        movieListData = new MovieRepository.MovieListData();
//        api = new MovieApi(movieListData, dao);
//    }
//
//    class MovieListData extends MutableLiveData<List<Movie>> {
//        public MovieListData () {
//            super();
//            setValue(new LinkedList<>());
//        }
//
//        @Override
//        protected void onActive() {
//            super.onActive();
//
//            new Thread(() -> {
//                List<Movie> movies = dao.index();
//                Log.d("MovieRepository", "Movies loaded from DB: " + movies);
//                postValue(movies);
//            }).start();
//        }
//    }
//
//    public LiveData<List<Movie>> getAll() {
//        return movieListData;
//    }
//
//    public Movie getMovie(String id) {
//        Movie movie = dao.get(id);
//        if (movie != null)
//            return movie;
//        return api.getMovie(id);
//    }
//
////    public void add (final Category category) {
////        api.add(category);
////    }
////
////    public void delete (final Category category) {
////        api.delete(category);
////    }
//
////    public void reload () {
////        api.reload();
////        api.getAllMovie();
////    }
//}