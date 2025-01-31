package com.example.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.androidapp.entities.User;

import com.example.androidapp.api.LoginResponse;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            Intent i = new Intent(this, NetflixActivity.class);
            startActivity(i);
        });
        Toast.makeText(this, "tost", Toast.LENGTH_LONG).show();

        binding.btnSignIn.setOnClickListener(v -> {
            String username = binding.editMailOrName.getText().toString();
            String password = binding.editPassword.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                User user = new User(username, password);
                UserApi userApi = new UserApi(this);
                userApi.loginUser(user, new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Log.d("Register", "Response raw body: " + response.raw().body());
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getToken() != null) {
                                String userId = loginResponse.getToken();

                                Log.d("Register", "User successfully logged i ");

                                Toast.makeText(LoginActivity.this, "User successfully logged in!", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(LoginActivity.this, ManagmentActivity.class);
                                i.putExtra("movieId", "679629522d6eaf038e9e1768");
                                startActivity(i);
                            }
                        } else if (response.code() == 404) {
                            Log.d("Register", "The user is not in the system ");

                            Toast.makeText(LoginActivity.this, "The user is not in the system, please check the login information", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Register", "Login error");

                            Toast.makeText(LoginActivity.this, "Login error, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.e("LoginActivity", "Error connecting to the server", t);

                        Toast.makeText(LoginActivity.this, "Error connecting to the server, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
                } else {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

        });

        binding.btnSignUp.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

    }
}