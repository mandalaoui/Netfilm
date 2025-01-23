package com.example.androidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.entities.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        private CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }
    }
    private final LayoutInflater mInfalter;
    private List<Category> categoryList;

    public CategoryAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
//        Category category = categoryList.get(position);
//        holder.categoryTitle.setText(category.getName());
//
//        // Set the adapter for movies in the category
//        MovieAdapter movieAdapter = new MovieAdapter(category.getMovies());
//        holder.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
//        holder.recyclerViewMovies.setAdapter(movieAdapter);
        if (categoryList != null) {
            final Category current = categoryList.get(position);
            holder.categoryTitle.setText(current.getName());
        }
    }
    public void setCategories(List<Category> s) {
        categoryList = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return categoryList.size();
        if (categoryList != null)
            return categoryList.size();
        return 0;
    }
}
