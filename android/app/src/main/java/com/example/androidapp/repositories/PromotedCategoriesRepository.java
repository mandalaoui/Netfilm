package com.example.androidapp.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.api.PromotedCategoryApi;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.entities.PromotedCategoryDao;
import com.example.androidapp.entities.LocalDatabase;

import java.util.LinkedList;
import java.util.List;

public class PromotedCategoriesRepository {
    private PromotedCategoryDao dao;
    private CategoryListData categoryListData;
    private PromotedCategoryApi api;

    public PromotedCategoriesRepository() {
        LocalDatabase db = LocalDatabase.getInstance(AppContext.getContext());
        dao = db.categoryDao();
        categoryListData = new CategoryListData();
        api = new PromotedCategoryApi(categoryListData, dao);
    }

    class CategoryListData extends MutableLiveData<List<PromotedCategory>> {
        public CategoryListData () {
            super();
            setValue(new LinkedList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
//                categoryListData.postValue(dao.index());
                List<PromotedCategory> categories = dao.index();
                Log.d("CategoryApi", "Categories loaded from DB: " + categories);
                postValue(categories);
            }).start();
        }
    }

    public LiveData<List<PromotedCategory>> getAll() {
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
