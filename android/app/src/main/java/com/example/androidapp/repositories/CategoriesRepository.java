package com.example.androidapp.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.api.CategoryApi;
import com.example.androidapp.entities.Category;
import com.example.androidapp.dao.CategoryDao;
import com.example.androidapp.entities.LocalDatabase;

import java.util.LinkedList;
import java.util.List;

public class CategoriesRepository {
    private CategoryDao dao;
    private CategoriesRepository.CategoryListData categoryListData;
    private CategoryApi api;

    // Constructor to initialize the DAO, API, and LiveData for Category list
    public CategoriesRepository() {
        LocalDatabase db = LocalDatabase.getInstance(MyApplication.getAppContext());
        dao = db.categoryDao();
        categoryListData = new CategoriesRepository.CategoryListData();
        api = new CategoryApi(categoryListData, dao);
    }

    // Inner class that extends MutableLiveData to hold the category list
    class CategoryListData extends MutableLiveData<List<Category>> {
        public CategoryListData () {
            super();
            setValue(new LinkedList<>());
        }
        // This method is triggered when the LiveData becomes active (e.g., observers are registered)
        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                List<Category> categories = dao.index();
                Log.d("CategoryApi", "Categories loaded from DB: " + categories);
                postValue(categories);
            }).start();
        }
    }

    // Method to get the LiveData object that holds the category list
    public LiveData<List<Category>> getAll() {
        return categoryListData;
    }

    // Method to add a new category by calling the API to add it
    public void add (final Category category) {
        api.add(category);
    }

    // Method to edit an existing category
    public void edit (final Category category) {
        new Thread(() -> {
            dao.update(category);
        }).start();

        api.edit(category);
    }

    // Method to delete an existing category
    public void delete (final Category category) {
        new Thread(() -> dao.delete(category)).start();
        api.delete(category);
    }

    // Method to reload the category list from the API and update the database
    public void reload () {
        api.getCategories();
    }
}
