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

    // Constructor to initialize the repository and fetch the list of promoted categories
    public PromotedCategoriesViewModel() {
        repository = new PromotedCategoriesRepository();
        categories = repository.getAll();
    }

    // Getter method to return the LiveData of promoted categories to be observed in UI
    public LiveData<List<PromotedCategory>> get() {
        return categories;
    }

    // Method to reload the promoted categories data, typically used for refreshing the list
    public void reload() {
        repository.reload();
    }
}
