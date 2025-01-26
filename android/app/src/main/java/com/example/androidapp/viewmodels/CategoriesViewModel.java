package com.example.androidapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.repositories.CategoriesRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesViewModel extends ViewModel {
    private CategoriesRepository repository;
    private LiveData<List<Category>> categories;

    public CategoriesViewModel() {
        repository = new CategoriesRepository();
        categories = repository.getAll();

//        categories.add(new Category("Action", Arrays.asList(new Movie("Movie 1"), new Movie("Movie 2"))));
//        categories.add(new Category("Comedy", Arrays.asList(new Movie("Movie 3"), new Movie("Movie 4"))));

    }

    public LiveData<List<Category>> get() {
        Log.d("CategoriesViewModel", "Categories list: " + categories.getValue());
        return categories;
    }

//    public void add(Category category) {
//        repository.add(category);
//    }
//
//    public void delete(Category category) {
//        repository.delete(category);
//    }

    public void reload() {
        Log.d("CategoriesViewModel", "Reloading categories...");
        repository.reload();
    }

    //chat
//    private LiveData<List<Category>> categories = new MutableLiveData<>(new ArrayList<>());;
//    private CategoriesRepository repository;
//
//    public CategoriesViewModel() {
////        categories = new MutableLiveData<>();
//        repository = new CategoriesRepository();
//    }
//
//    public LiveData<List<Category>> getCategories() {
//        return categories;
//    }
//
//    public void loadCategories() {
//        repository.fetchCategories(categories);
//    }
}
