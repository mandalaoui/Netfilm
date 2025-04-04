package com.example.androidapp.api;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.dao.PromotedCategoryDao;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromotedCategoryApi {

    private MutableLiveData<List<PromotedCategory>> categoryListData;
    private PromotedCategoryDao dao;
    Retrofit retrofit;
    ApiService apiService;

    // Constructor that initializes the LiveData, DAO, Retrofit, and ApiService
    public PromotedCategoryApi(MutableLiveData<List<PromotedCategory>> categoryListData, PromotedCategoryDao dao) {
        this.categoryListData = categoryListData;
        this.dao = dao;

        // Create a Retrofit instance with the base URL and Gson converter
        retrofit = new Retrofit.Builder()
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    MyApplication myApplication = MyApplication.getInstance();
    String userId = myApplication.getGlobalUserId();

    // Method to fetch the list of promoted categories from the API
    public void getCategories() {
        Call<List<PromotedCategory>> call = apiService.getCategories(userId);
        call.enqueue(new Callback<List<PromotedCategory>>() {
            @Override
            public void onResponse(Call<List<PromotedCategory>> call, Response<List<PromotedCategory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Run this block on a separate thread to avoid blocking the UI thread
                    new Thread(() -> {
                        dao.clear();
                        dao.insertList(response.body());
                        categoryListData.postValue(dao.index());
                    }).start();
                }
                else {
                    Log.d("CategoryApi", "Failed to get categories: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<PromotedCategory>> call, Throwable t) {
                Log.e("CategoryApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }

}
