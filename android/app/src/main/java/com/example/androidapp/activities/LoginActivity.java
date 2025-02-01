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
                UserApi userApi = new UserApi();
                userApi.loginUser(user);
            }
            else {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }

        });

        binding.btnSignUp.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });

    }
}