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

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        RecyclerView lstMovies;
        private MovieListAdapter movieListAdapter;
        private CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            lstMovies = itemView.findViewById(R.id.lstMovies);
            movieListAdapter = new MovieListAdapter(context);
            lstMovies.setAdapter(movieListAdapter);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false);
            lstMovies.setLayoutManager(gridLayoutManager);
        }
        public void bindMovies(List<Movie> movies) {
            if (movieListAdapter != null) {
                movieListAdapter.setMovies(movies);
            }
        }
    }

    private final LayoutInflater mInfalter;
    private List<PromotedCategory> categories;
    private Context context;

    public PromotedCategoryListAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.promoted_category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        if (categories != null) {
            final PromotedCategory current = categories.get(position);
            Log.d("CategoryListAdapter", "Binding category: " + (current != null ? current.getCategoryName() : "null"));
            Log.d("CategoryListAdapter", "Movies for category " + current.getCategoryName() + ": " + current.getMovies());
            holder.categoryTitle.setText(current.getCategoryName());
//            holder.bindMovies(current.getMovies());
            if (current.getMovies() != null && !current.getMovies().isEmpty()) {
                holder.bindMovies(current.getMovies());
            } else {
                Log.d("CategoryListAdapter", "No movies available for this category");
            }
        }
    }
    public void setCategories(List<PromotedCategory> categories) {
        Log.d("CategoryListAdapter", "Categories set in adapter: " + categories.size());
        this.categories = categories;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (categories != null)
            return categories.size();
        return 0;
    }

    public List<PromotedCategory> getCategories() {
        return categories;
    }
}
