package com.example.androidapp.activities;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidapp.MyApplication;
import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.adapters.PromotedCategoryListAdapter;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.databinding.ActivityHomeBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.example.androidapp.viewmodels.PromotedCategoriesViewModel;
import com.google.android.material.appbar.AppBarLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isDarkMode = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel for categories and observe changes
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.get().observe(this, categories -> {
            allCategories = categories;
        });

        // Initialize ViewModel for promoted categories
        promotedCategoriesViewModel = new ViewModelProvider(this).get(PromotedCategoriesViewModel.class);

        // Set up the RecyclerView for promoted categories
        RecyclerView lstCategories = binding.lstCategories;
        final PromotedCategoryListAdapter promotedCategoryListAdapter = new PromotedCategoryListAdapter(this);
        lstCategories.setAdapter(promotedCategoryListAdapter);
        lstCategories.setLayoutManager(new LinearLayoutManager(this));

        // Set behavior for the RecyclerView layout with CoordinatorLayout
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lstCategories.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        lstCategories.setLayoutParams(params);

        // Set up the random movie player view
        PlayerView random_movie = binding.randomMovie;
        promotedCategoriesViewModel.get().observe(this, categories -> {
            promotedCategoryListAdapter.setCategories(categories);

            // Play a random movie if categories exist
            if (!categories.isEmpty()) {
                Movie randMovie = getRandMovie(categories);
                ExoPlayer exoPlayer = new ExoPlayer.Builder(this).build();
                random_movie.setPlayer(exoPlayer);

                String videoUrl = "http://10.0.2.2:12345/api/" + randMovie.getVideo();
                if (videoUrl != null) {
                    MediaItem mediaItem = MediaItem.fromUri(videoUrl);

                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.prepare();
                    exoPlayer.play();
                } else {
                    Log.e("HomeActivity", "Video URL is null or empty");
                }
            }
        });

        // Set the "exit" button to show a confirmation dialog
        ImageButton exitBtn = binding.exit;
        exitBtn.setOnClickListener(v -> showExitDialog());

        // Set the "home" button to scroll to top and expand the app bar
        ImageButton home = binding.home;
        home.setOnClickListener(v -> {
            lstCategories.scrollToPosition(0);
            AppBarLayout appBarLayout = binding.menu;
            appBarLayout.setExpanded(true, true);
        });

        // Set up the admin button visibility and actions
        ImageButton adminBtn = binding.admin;
        MyApplication myApplication = MyApplication.getInstance();
        if (myApplication.isAdmin()) {
            adminBtn.setOnClickListener(v -> showAdminMenu(adminBtn));
        } else {
            adminBtn.setVisibility(View.GONE);
        }

        // Set up the RecyclerView for searched movies
        searchedMovies = binding.searchedMovies;
        searchedMovies.setVisibility(View.GONE);
        movieListAdapter = new MovieListAdapter(this);
        searchedMovies.setAdapter(movieListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        searchedMovies.setLayoutManager(gridLayoutManager);

        // Initialize the search view and handle search input changes
        searchView = binding.search;
        LinearLayout linearLayout1 = (LinearLayout) searchView.getChildAt(0);
        LinearLayout linearLayout2 = (LinearLayout) linearLayout1.getChildAt(2);
        LinearLayout linearLayout3 = (LinearLayout) linearLayout2.getChildAt(1);
        AutoCompleteTextView autoComplete = (AutoCompleteTextView) linearLayout3.getChildAt(0);
        autoComplete.setTextColor(Color.WHITE);

        // Set the query text listener for the search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMovie(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchView.setVisibility(View.VISIBLE);
                if (newText.isEmpty()) {
                    searchedMovies.setVisibility(View.GONE);
                } else {
                    searchedMovies.setVisibility(View.VISIBLE);
                    searchMovie(newText);
                }
                return false;
            }
        });
        // Button to show category selection dialog
        Button btnCategories = binding.btnCategories;
        btnCategories.setOnClickListener(v -> showCategoriesDialog());
    }

    // Display a popup menu with admin options
    private void showAdminMenu(ImageButton adminBtn) {
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, adminBtn);
        Menu menu = popupMenu.getMenu();

        // Add admin menu items
        menu.add(0, 1, 0, "Add Movie");
        menu.add(0, 2, 1, "Edit Movie");
        menu.add(0, 3, 2, "Delete Movie");
        menu.add(0, 4, 3, "Add Category");
        menu.add(0, 5, 4, "Edit Category");
        menu.add(0, 6, 5, "Delete Category");

        // Set click listener for menu items
        popupMenu.setOnMenuItemClickListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case 1:
                    intent = new Intent(HomeActivity.this, CreateMovieActivity.class);
                    break;
                case 2:
                    intent = new Intent(HomeActivity.this, ManagementActivity.class);
                    break;
                case 3:
                    intent = new Intent(HomeActivity.this, DeleteMovieActivity.class);
                    break;
                case 4:
                    intent = new Intent(HomeActivity.this, CreateCategoryActivity.class);
                    break;
                case 5:
                    intent = new Intent(HomeActivity.this, ManagementActivity.class);
                    break;
                case 6:
                    intent = new Intent(HomeActivity.this, ManagementActivity.class);
                    break;
                default:
                    intent = new Intent(HomeActivity.this, HomeActivity.class);
            }
            startActivity(intent);
            return true;
        });
        popupMenu.show();
    }

    // Show a dialog with category options for the user to choose
    private void showCategoriesDialog() {
        categoriesViewModel.reload();

        List<String> categoryNames = new ArrayList<>();
        for (Category category : allCategories) {
            categoryNames.add(category.getName());
        }

        // Show category selection dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose category")
                .setItems(categoryNames.toArray(new String[0]), (dialog, which) -> {
                    Category selectedCategory = allCategories.get(which);
                    openCategoryScreen(selectedCategory);
                })
                .setNegativeButton("X", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Open the selected category screen
    private void openCategoryScreen(Category selectedCategory) {
        ArrayList<String> moviesArrayList = new ArrayList<>(selectedCategory.getMovies());
        Intent intent = new Intent(HomeActivity.this, SelectedCategoryActivity.class);
        intent.putStringArrayListExtra("movies", moviesArrayList);
        intent.putExtra("name", selectedCategory.getName());
        startActivity(intent);
    }

    // Method to search for movies based on query
    private void searchMovie(String query) {
        MovieApi movieApi = new MovieApi();
        movieApi.getSearchedMovies(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body();
                    movieListAdapter.setMovies(response.body());
                } else {
                    Log.e("API Response", "Response error: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API Failure", "Failed to load searched movies: " + t.getMessage());
                movieListAdapter.setMovies(null);
            }
        }, query);
    }

    // Get a random movie from the promoted categories
    private Movie getRandMovie (List<PromotedCategory> categories) {
        Random rand = new Random();
        int randomIndex;
        PromotedCategory randCategory;
        do {
            randomIndex = rand.nextInt(categories.size());
            randCategory = categories.get(randomIndex);
        } while (randCategory.getMovies().isEmpty());
        randomIndex = rand.nextInt(randCategory.getMovies().size());
        return randCategory.getMovies().get(randomIndex);
    }

    // Show the exit confirmation dialog
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