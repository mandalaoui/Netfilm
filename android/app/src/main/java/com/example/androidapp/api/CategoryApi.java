package com.example.androidapp.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
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
//                    Log.d("CategoryApi", "Received categories: " + response.body());
//                    if (response.body() != null && !response.body().isEmpty()) {
//                        for (Category category : response.body()) {
//                            Log.d("CategoryApi", "Category: " + category.getName() + "," + category.getMovies());
//                        }
//                    }
                    new Thread(() -> {
                        Log.d("CategoryApi", "Thread started");
                        dao.clear();
                        dao.insertList(response.body());
                        Log.d("CategoryApi", "Inserted categories into DB: " + response.body());
                        categoryListData.postValue(dao.index());
                    }).start();
                }
                else {
                    Toast.makeText(MyApplication.getAppContext(), "Failed to get categories: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("CategoryApi", "Error fetching categories: " + t.getMessage());
            }
        });
    }

    public void add(Category category) {
        Call<Category> call = apiService.createCategory(userId, category);
        call.enqueue(new Callback<Category>() {
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insert(response.body());
                    }).start();
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Failed to create category:" + response.message(), Toast.LENGTH_SHORT).show();
                    try {
                        Toast.makeText(MyApplication.getAppContext(), "Error Response" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(MyApplication.getAppContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void delete (Category category) {
        String categoryId = category.getId();
        Call<Category> call = apiService.deleteCategory(categoryId,userId);
        call.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()) {
                    Log.d("CategoryApi", "Category delete successfully");
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Error Response" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryApi", "Error delete Category: " + t.getMessage());
            }
        });
    }

    public void edit(Category category) {
        Call<Category> call = apiService.editCategory(userId, category.getId(), category);
        call.enqueue(new Callback<Category>() {
            public void onResponse(Call<Category> call, retrofit2.Response<Category> response) {
                if (response.isSuccessful()) {
                    Log.d("CategoryApi", "Category update successfully");
                } else {
                    Toast.makeText(MyApplication.getAppContext(), "Failed to update category:" + response.message(), Toast.LENGTH_SHORT).show();
                    try {
                        Toast.makeText(MyApplication.getAppContext(), "Error Response" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Log.e("CategoryApi", "Error: " + t.getMessage());
                Toast.makeText(MyApplication.getAppContext(), "Network request failed", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
