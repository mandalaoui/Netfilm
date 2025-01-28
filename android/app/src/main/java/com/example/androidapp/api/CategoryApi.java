package com.example.androidapp.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.R;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.CategoryDao;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryApi {

    private MutableLiveData<List<Category>> categoryListData;
    private CategoryDao dao;

    Retrofit retrofit;
    ApiService apiService;

    public CategoryApi(MutableLiveData<List<Category>> categoryListData, CategoryDao dao) {
        this.categoryListData = categoryListData;
        this.dao = dao;

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    String userId = "6792b52c10a40e0b80dd798d";
    public void getCategories() {
        Call<List<Category>> call = apiService.getCategories(userId);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CategoryApi", "Received categories: " + response.body());
                    if (response.body() != null && !response.body().isEmpty()) {
                        for (Category category : response.body()) {
                            Log.d("CategoryApi", "Category: " + category.getCategoryName() + "," + category.getMovies());
                        }
                    }
                    new Thread(() -> {
                        Log.d("CategoryApi", "Thread started");
                        dao.clear();
                        dao.insertList(response.body());
                        Log.d("CategoryApi", "Inserted categories into DB: " + response.body());
                        categoryListData.postValue(dao.index());
                    }).start();
                }
                else {
                    Log.d("CategoryApi", "Failed to get categories: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("CategoryApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }

}
