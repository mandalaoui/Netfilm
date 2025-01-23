package com.example.androidapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private CategoryRepository mRepository;
    private LiveData<List<Category>> categories;

    public CategoryViewModel() {
        mRepository = new CategoryRepository();

        //רק את מה שצריך
        categories = mRepository.getAll();
    }

    public LiveData<List<Category>> get() {
        return categories;
    }

    public void add(Category category) {
        mRepository.add(category);
    }

    public void delete(Category categor) {
        mRepository.delete(categor);
    }

    public void reload() {
        mRepository.reload();
    }
}
