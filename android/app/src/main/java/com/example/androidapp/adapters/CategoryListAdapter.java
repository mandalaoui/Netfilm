package com.example.androidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.entities.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
//        RecyclerView recyclerViewMovies;
        private CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
//            recyclerViewMovies = itemView.findViewById(R.id.recyclerViewMovies);
        }
    }

    private final LayoutInflater mInfalter;
    private List<Category> categories;
    private Context context;

    public CategoryListAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.category_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        if (categories != null) {
            final Category current = categories.get(position);
            Log.d("CategoryListAdapter", "Binding category: " + (current != null ? current.getCategoryName() : "null"));
            holder.categoryTitle.setText(current.getCategoryName());

//            MovieAdapter movieAdapter = new MovieAdapter(context);
//            holder.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
//            holder.recyclerViewMovies.setAdapter(movieAdapter);
        }
    }
    public void setCategories(List<Category> categories) {
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

    public List<Category> setCategories() {
        return categories;
    }


    //chat
//    private List<Category> categories;
//    private Context context;
//
//    public CategoryListAdapter(Context context, List<Category> categories) {
//        this.context = context;
//        this.categories = categories;
//    }
//
//    @NonNull
//    @Override
//    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
//        return new CategoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
//        Category category = categories.get(position);
//        holder.categoryName.setText(category.getName());
//
//        MovieAdapter movieAdapter = new MovieAdapter(context, category.getMovies());
//        holder.movieRecyclerView.setAdapter(movieAdapter);
//    }
//
//    @Override
//    public int getItemCount() {
//        return categories.size();
//    }
//
//    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
//        TextView categoryName;
//        RecyclerView movieRecyclerView;
//
//        public CategoryViewHolder(View itemView) {
//            super(itemView);
//            categoryName = itemView.findViewById(R.id.categoryTitle);
//            movieRecyclerView = itemView.findViewById(R.id.recyclerViewMovies);
//            movieRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
//        }
//    }
}
