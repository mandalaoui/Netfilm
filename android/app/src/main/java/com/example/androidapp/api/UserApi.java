package com.example.androidapp.api;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.androidapp.AppContext;
import com.example.androidapp.R;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.User;
import com.example.androidapp.repositories.CategoriesRepository;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private Retrofit retrofit;
    private ApiService apiService;

    public UserApi() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(AppContext.getContext().getString(R.string.BaseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public void registerUser(User user, final Callback<User> callback) {

        Log.d("API_REQUEST", "Sending registration request for user: " + user.getUsername());

        Call<User> call = apiService.post(user);
        call.enqueue(callback);
    }

    public void loginUser(User user, final Callback<ApiResponse> callback) {
        Log.d("API_REQUEST", "Sending registration request for user: 1" + user.getUsername());
        Call<ApiResponse> call = apiService.login(user);
        call.enqueue(callback);
    }

//    public void getCtegories(MutableLiveData<List<Category>> categories) {
//        Call<List<Category>> call = apiService.getCategories();
//        call.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//                categories.setValue(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//                Log.i("UserApi","fail response");
//            }
//        });
//    }
}
