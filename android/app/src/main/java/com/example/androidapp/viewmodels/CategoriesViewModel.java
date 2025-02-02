package com.example.androidapp.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.repositories.CategoriesRepository;
import com.example.androidapp.repositories.PromotedCategoriesRepository;

import java.util.List;

public class CategoriesViewModel extends ViewModel  {
    private CategoriesRepository repository;
    private LiveData<List<Category>> categories;

    public CategoriesViewModel() {
        repository = new CategoriesRepository();
        categories = repository.getAll();
    }

    public LiveData<List<Category>> get() {
        Log.d("CategoriesViewModel", "Categories list: " + categories.getValue());
        return categories;
    }

    public void add(Category category) {
        repository.add(category);
    }
//
//    public void delete(Category category) {
//        repository.delete(category);
//    }

    public void reload() {
        Log.d("CategoriesViewModel", "Reloading categories...");
        repository.reload();
    }
}
