package com.example.androidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.entities.PromotedCategory;
import com.example.androidapp.entities.Movie;

import java.util.List;

public class PromotedCategoryListAdapter extends RecyclerView.Adapter<PromotedCategoryListAdapter.CategoryViewHolder> {

    // ViewHolder class to hold category title and associated movie list
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView lstMovies;
        private MovieListAdapter movieListAdapter;
        private CategoryViewHolder(View itemView) {
            super(itemView);
            // Initialize views for category title and movie list
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            lstMovies = itemView.findViewById(R.id.lstMovies);

            // Initialize movie list adapter and set it to the RecyclerView
            movieListAdapter = new MovieListAdapter(context);
            lstMovies.setAdapter(movieListAdapter);

            // Set up the RecyclerView layout manager for horizontal scrolling
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false);
            lstMovies.setLayoutManager(gridLayoutManager);
        }

        // Method to bind movies to the movie list in the category
        public void bindMovies(List<Movie> movies) {
            if (movieListAdapter != null) {
                movieListAdapter.setMovies(movies);
            }
        }
    }

    private final LayoutInflater mInfalter;
    private List<PromotedCategory> categories;
    private Context context;

    // Constructor for the adapter, takes in context
    public PromotedCategoryListAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
        this.context = context;
    }

    // Method to create a new ViewHolder for a category
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.promoted_category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    // Method to bind the data for a category to the ViewHolder
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        if (categories != null) {
            final PromotedCategory current = categories.get(position);
            holder.categoryTitle.setText(current.getCategoryName());

            // If there are movies in the category, bind them to the RecyclerView
            if (current.getMovies() != null && !current.getMovies().isEmpty()) {
                holder.bindMovies(current.getMovies());
            } else {
                Log.d("CategoryListAdapter", "No movies available for this category");
            }
        }
    }

    // Method to update the list of categories in the adapter
    public void setCategories(List<PromotedCategory> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    // Get the total number of categories in the list
    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }

    // Get the list of promoted categories
    public List<PromotedCategory> getCategories() {
        return categories;
    }
}
