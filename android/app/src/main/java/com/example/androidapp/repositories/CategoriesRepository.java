package com.example.androidapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
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

    public CategoriesRepository() {
        LocalDatabase db = LocalDatabase.getInstance(AppContext.getContext());
        dao = db.categoryDao();
        categoryListData = new CategoriesRepository.CategoryListData();
        api = new CategoryApi(categoryListData, dao);
    }

    class CategoryListData extends MutableLiveData<List<Category>> {
        public CategoryListData () {
            super();
            setValue(new LinkedList<>());
        }

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

    public LiveData<List<Category>> getAll() {
        return categoryListData;
    }

//    public void add (final Category category) {
//        api.add(category);
//    }
//
//    public void delete (final Category category) {
//        api.delete(category);
//    }

    public void reload () {
//        api.reload();
        api.getCategories();
    }
}
