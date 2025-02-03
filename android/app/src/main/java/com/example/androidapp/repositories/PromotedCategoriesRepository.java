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

    public PromotedCategoriesRepository() {
        LocalDatabase db = LocalDatabase.getInstance(MyApplication.getAppContext());
        dao = db.promotedCategoryDao();
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
                List<PromotedCategory> categories = dao.index();
                Log.d("PromotedCategoryApi", "Categories loaded from DB: " + categories);
                postValue(categories);
            }).start();
        }
    }

    public LiveData<List<PromotedCategory>> getAll() {
        return categoryListData;
    }


    public void reload () {
        api.getCategories();
    }
}
