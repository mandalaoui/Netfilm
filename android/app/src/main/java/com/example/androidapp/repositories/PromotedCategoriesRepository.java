package com.example.androidapp.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//import com.example.androidapp.AppContext;
import com.example.androidapp.MyApplication;
import com.example.androidapp.api.PromotedCategoryApi;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.dao.PromotedCategoryDao;
import com.example.androidapp.entities.LocalDatabase;

import java.util.LinkedList;
import java.util.List;

public class PromotedCategoriesRepository {
    private PromotedCategoryDao dao;
    private CategoryListData categoryListData;
    private PromotedCategoryApi api;

    // Constructor initializes the repository by setting up the local database, API, and LiveData
    public PromotedCategoriesRepository() {
        LocalDatabase db = LocalDatabase.getInstance(MyApplication.getAppContext());
        dao = db.promotedCategoryDao();
        categoryListData = new CategoryListData();
        api = new PromotedCategoryApi(categoryListData, dao);
    }

    // Inner class that extends MutableLiveData to hold the list of promoted categories
    class CategoryListData extends MutableLiveData<List<PromotedCategory>> {
        public CategoryListData () {
            super();
            setValue(new LinkedList<>());
        }

        // This method is called when the LiveData becomes active (i.e., when observers are registered)
        @Override
        protected void onActive() {
            super.onActive();
            // Perform background task to load promoted categories from the local database
            new Thread(() -> {
                List<PromotedCategory> categories = dao.index();
                Log.d("PromotedCategoryApi", "Categories loaded from DB: " + categories);
                postValue(categories);
            }).start();
        }
    }

    // Method to get the LiveData object that holds the list of promoted categories
    public LiveData<List<PromotedCategory>> getAll() {
        return categoryListData;
    }

    // Method to reload the promoted categories by fetching them from the remote API
    public void reload () {
        api.getCategories();
    }
}
