package com.example.androidapp.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.repositories.PromotedCategoriesRepository;

import java.util.List;

public class PromotedCategoriesViewModel extends ViewModel {
    private PromotedCategoriesRepository repository;
    private LiveData<List<PromotedCategory>> categories;

    public PromotedCategoriesViewModel() {
        repository = new PromotedCategoriesRepository();
        categories = repository.getAll();
    }

    public LiveData<List<PromotedCategory>> get() {
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
        Log.d("PromotedCategoriesViewModel", "Reloading categories...");
        repository.reload();
    }
}
