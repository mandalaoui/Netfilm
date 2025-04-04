package com.example.androidapp.api;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
import com.example.androidapp.activities.HomeActivity;
import com.example.androidapp.entities.Category;
import com.example.androidapp.dao.CategoryDao;

import java.io.IOException;
import java.util.List;

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
                .baseUrl(MyApplication.getAppContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    MyApplication myApplication = MyApplication.getInstance();

    String userId = myApplication.getGlobalUserId();

    public void getCategories() {
        Call<List<Category>> call = apiService.getAllCategories(userId);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        Log.d("CategoryApi", "Thread started");
                        dao.clear();
                        dao.insertList(response.body());
                        Log.d("CategoryApi", "Inserted categories into DB: " + response.body());
                        categoryListData.postValue(dao.index());
                    }).start();
                }
                else {
                    Log.e("CategoryApi", "Failed to get categories: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("CategoryApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }
    // Method to add a new category
    public void add(Category category) {
        Call<Category> call = apiService.createCategory(userId, category);
        call.enqueue(new Callback<Category>() {
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insert(response.body());
                    }).start();
                } else {
                    try {
                        Toast.makeText(MyApplication.getAppContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent(MyApplication.getAppContext(), HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getAppContext().startActivity(i);
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryApi", "Network request failed");
            }
        });

    }
    // Method to delete a category
    public void delete (Category category) {
        String categoryId = category.getId();
        Call<Category> call = apiService.deleteCategory(categoryId,userId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Log.d("CategoryApi", "Category delete successfully");
                    Intent i = new Intent(MyApplication.getAppContext(), HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } else {
                    Toast.makeText(MyApplication.getAppContext(), response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryApi", "Error delete Category: " + t.getMessage());
            }
        });
    }

    // Method to edit a category
    public void edit(Category category) {
        Call<Category> call = apiService.editCategory(userId, category.getId(), category);
        call.enqueue(new Callback<Category>() {
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                if (response.isSuccessful()) {
                    Log.d("CategoryApi", "Category update successfully");
                    Intent i = new Intent(MyApplication.getAppContext(), HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyApplication.getAppContext().startActivity(i);
                } else {
                    try {
                        Toast.makeText(MyApplication.getAppContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryApi", "Error: " + t.getMessage());
            }
        });
    }
}
