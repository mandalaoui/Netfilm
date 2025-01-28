package com.example.androidapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.PromotedCategoryListAdapter;
import com.example.androidapp.databinding.ActivityHomeBinding;
import com.example.androidapp.viewmodels.PromotedCategoriesViewModel;
import com.google.android.material.appbar.AppBarLayout;

public class HomeActivity extends AppCompatActivity {
    private PromotedCategoriesViewModel promotedCategoriesViewModel;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                Log.d("HomeActivity", menu.getItem(item.getItemId() - 1).toString());
                Intent intent = new Intent(HomeActivity.this, ManagementActivity.class);
                intent.putExtra("option_id", item.getItemId());
                startActivity(intent);
                return true;
            });
            popupMenu.show();
        });

        SearchView searchView = binding.search;
//        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
//        searchEditText.setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // אם המשתמש שולח את החיפוש
                Log.d("Search", "Query submitted: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // עדכון בזמן שהמשתמש מקליד
                Log.d("Search", "Query changed: " + newText);
                // כאן תוכל להוסיף לוגיקה כדי להציג תוצאות חיפוש
                return false;
            }
        });
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sign out")
                .setMessage("Are you sure you to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
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