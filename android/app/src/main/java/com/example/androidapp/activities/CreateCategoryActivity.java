    package com.example.androidapp.activities;

    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Spinner;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.androidapp.entities.Category;
    import com.example.androidapp.R;
    import com.example.androidapp.adapters.MovieAdapter;
    import com.example.androidapp.databinding.ActivityCreateCategoryBinding;
    import com.example.androidapp.entities.Movie;
    import com.example.androidapp.viewmodels.CategoriesViewModel;
    import com.example.androidapp.viewmodels.MovieViewModel;
    import com.google.android.material.switchmaterial.SwitchMaterial;

    import java.util.ArrayList;
    import java.util.List;

    public class CreateCategoryActivity extends AppCompatActivity {
        private ActivityCreateCategoryBinding binding;
        private EditText create_nameCategory;
        private SwitchMaterial create_isPromotedSwitch;
        private Button btnCreateCategory;
        private RecyclerView recyclerViewMovies;
        private MovieViewModel movieViewModel;
        private MovieAdapter movieAdapter;
        private CategoriesViewModel categoriesViewModel;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_create_category);
            binding = ActivityCreateCategoryBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            btnCreateCategory = binding.createCategoryButton;
            create_nameCategory = binding.categoryNameInput;
            create_isPromotedSwitch = binding.isPromotedSwitch;

            categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

            recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
            recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3));
            movieAdapter = new MovieAdapter(this, new ArrayList<>(), false);
            recyclerViewMovies.setAdapter(movieAdapter);

            // Initialize the MovieViewModel and reload data
            movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
            movieViewModel.reload();

            // Observe movie data from the ViewModel and update the adapter
            movieViewModel.get().observe(this,movies -> {
                movieAdapter.setMovies(movies);
            });

            // Set up the button click listener to create a new category
            btnCreateCategory.setOnClickListener(v -> {
                String categoryName = create_nameCategory.getText().toString();
                boolean isPromoted = create_isPromotedSwitch.isChecked();
                List<String> selectedIds = movieAdapter.getSelectedMovieIds();

                if (categoryName.isEmpty()) {
                    Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                Category category = new Category(categoryName, isPromoted, selectedIds);
                categoriesViewModel.add(category);
                Toast.makeText(this, "Category created", Toast.LENGTH_SHORT).show();
            });

        }

        @Override
        public void onResume() {
            super.onResume();
            movieViewModel.reload();
        }


    }