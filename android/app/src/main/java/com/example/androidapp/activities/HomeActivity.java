package com.example.androidapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.adapters.PromotedCategoryListAdapter;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.databinding.ActivityHomeBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.example.androidapp.viewmodels.PromotedCategoriesViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private PromotedCategoriesViewModel promotedCategoriesViewModel;
    private ActivityHomeBinding binding;
    private RecyclerView searchedMovies;
    private MovieListAdapter movieListAdapter;
    private CategoriesViewModel categoriesViewModel;
    private List<Category> allCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.get().observe(this, categories -> {
            Log.d("HomeActivity", "Categories list: " + categories);
            allCategories = categories;
        });

        promotedCategoriesViewModel = new ViewModelProvider(this).get(PromotedCategoriesViewModel.class);

        RecyclerView lstCategories = binding.lstCategories;
        final PromotedCategoryListAdapter promotedCategoryListAdapter = new PromotedCategoryListAdapter(this);
        lstCategories.setAdapter(promotedCategoryListAdapter);
        lstCategories.setLayoutManager(new LinearLayoutManager(this));

        promotedCategoriesViewModel.reload();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lstCategories.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        lstCategories.setLayoutParams(params);

        promotedCategoriesViewModel.get().observe(this, categories -> {
            Log.d("HomeActivity", "Categories list: " + categories);
            promotedCategoryListAdapter.setCategories(categories);
        });

        ImageButton exitBtn = binding.exit;
        exitBtn.setOnClickListener(v -> showExitDialog());

        ImageButton home = binding.home;
        home.setOnClickListener(v -> {
            lstCategories.scrollToPosition(0);
            AppBarLayout appBarLayout = binding.menu;
            appBarLayout.setExpanded(true, true);
        });

        ImageButton adminBtn = binding.admin;
        adminBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(HomeActivity.this, adminBtn);

            Menu menu = popupMenu.getMenu();

            menu.add(0, 1, 0, "Add Movie");
            menu.add(0, 2, 1, "Edit Movie");
            menu.add(0, 3, 2, "Delete Movie");
            menu.add(0, 4, 3, "Add Category");
            menu.add(0, 5, 4, "Edit Category");
            menu.add(0, 6, 5, "Delete Category");

            popupMenu.setOnMenuItemClickListener(item -> {
                Intent intent;
                Log.d("HomeActivity", menu.getItem(item.getItemId() - 1).toString());
                switch (item.getItemId()) {
                    case 1:
                        intent = new Intent(HomeActivity.this, CreateMovieActivity.class);
                        break;
                    case 2:
                        intent = new Intent(HomeActivity.this, EditMovieActivity.class);
                        break;
                    case 3:
                        intent = new Intent(HomeActivity.this, DeleteMovieActivity.class);
                        break;
                    case 4:
                        intent = new Intent(HomeActivity.this, CreateCategoryActivity.class);
                        break;
                    case 5:
                        intent = new Intent(HomeActivity.this, EditCategoryActivity.class);
                        break;
                    case 6:
                        intent = new Intent(HomeActivity.this, DeleteCategory.class);
                        break;
                    default:
                        intent = new Intent(HomeActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                return true;
            });
            popupMenu.show();
        });

        searchedMovies = binding.searchedMovies;
        searchedMovies.setVisibility(View.GONE);
        movieListAdapter = new MovieListAdapter(this);
        searchedMovies.setAdapter(movieListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        searchedMovies.setLayoutManager(gridLayoutManager);

        SearchView searchView = binding.search;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Search", "Query submitted: " + query);
                searchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.setVisibility(View.VISIBLE);
                // עדכון בזמן שהמשתמש מקליד
                Log.d("Search", "Query changed: " + newText);
                if (newText.isEmpty()) {
                    searchedMovies.setVisibility(View.GONE);
                } else {
                    searchedMovies.setVisibility(View.VISIBLE);
                    searchMovie(newText);
                }
                return false;
            }
        });

        Button btnCategories = binding.btnCategories;
        btnCategories.setOnClickListener(v -> showCategoriesDialog());
    }

    private void showCategoriesDialog() {
        categoriesViewModel.reload();

        List<String> categoryNames = new ArrayList<>();
        for (Category category : allCategories) {
            Log.d("HomeActivity", "category name" + category.getName());
            categoryNames.add(category.getName());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose category")
                .setItems(categoryNames.toArray(new String[0]), (dialog, which) -> {
                    // ברגע שהמשתמש בחר קטגוריה
//                    String selectedCategory = categoryNames.get(which);
//                    openCategoryScreen(selectedCategory); // קרא לפונקציה שתפתח את המסך המתאים
                    Category selectedCategory = allCategories.get(which);  // קח את האובייקט המתאים
                    openCategoryScreen(selectedCategory);
                })
                .setNegativeButton("X", (dialog, which) -> dialog.dismiss()) // כפתור ביטול
                .create()
                .show();
    }

    private void openCategoryScreen(Category selectedCategory) {
        Log.d("HomeActivity dialog", selectedCategory.getName());
    }

    private void searchMovie(String query) {
        MovieApi movieApi = new MovieApi();
        movieApi.getSearchedMovies(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    Log.d("API Response", "Searched movies received: " + movies.toString());
                    movieListAdapter.setMovies(response.body());
                } else {
                    Log.e("API Response", "Response error: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API Failure", "Failed to load searched movies: " + t.getMessage());
            }
        }, query);
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign out")
                .setMessage("Are you sure you to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(HomeActivity.this, NetflixActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        promotedCategoriesViewModel.reload();
    }
}