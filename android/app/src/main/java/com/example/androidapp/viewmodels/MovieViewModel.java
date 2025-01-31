//package com.example.androidapp.viewmodels;
//
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.androidapp.entities.Movie;
//import com.example.androidapp.repositories.MovieRepository;
//
//import java.util.List;
//
//public class MovieViewModel extends ViewModel {
//    private MovieRepository repository;
//    private LiveData<List<Movie>> movies;
////    private Movie movie;
//
//    public MovieViewModel() {
//        repository = new MovieRepository();
//        movies = repository.getAll();
//    }
//
//    public LiveData<List<Movie>> getMovies() {
//        Log.d("CategoriesViewModel", "Categories list: " + movies.getValue());
//        return movies;
//    }
//
////    public Movie getMovie() {
////        return repository.
////    }
//
////    public void add(Category category) {
////        repository.add(category);
////    }
////
////    public void delete(Category category) {
////        repository.delete(category);
////    }
//
//    public void reload() {
////        repository.reload();
//    }
//}