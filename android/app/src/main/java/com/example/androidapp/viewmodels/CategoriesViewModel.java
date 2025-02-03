package com.example.androidapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Category;
import com.example.androidapp.repositories.CategoriesRepository;

import java.util.List;

public class CategoriesViewModel extends ViewModel  {
    private CategoriesRepository repository;
    private LiveData<List<Category>> categories;

    // Constructor to initialize the repository and fetch the categories
    public CategoriesViewModel() {
        repository = new CategoriesRepository();
        categories = repository.getAll();
    }

    // Getter method to return the LiveData of categories to be observed in UI
    public LiveData<List<Category>> get() {
        return categories;
    }

    // Method to add a category via the repository
    public void add(Category category) {
        repository.add(category);
    }

    // Method to edit an existing category via the repository
    public void edit(Category category) {
        repository.edit(category);
    }

    // Method to delete a category via the repository
    public void delete(Category category) {
        repository.delete(category);
    }

    // Method to reload the data, typically used for refreshing the categories
    public void reload() {
        repository.reload();
    }
}
