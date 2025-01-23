package com.example.androidapp.adapters;

import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_movie;
        private MovieViewHolder(View itemView) {
            super(itemView);
            iv_movie = itemView.findViewById(R.id.iv_movie);
        }
    }
    private final LayoutInflater mInfalter;
    private List<Movie> movieList;

    public MovieAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (movieList != null) {
            final Movie current = movieList.get(position);
            holder.iv_movie.setImageResource(R.drawable.movie_image);
        }
    }

    public void setMovies(List<Movie> s) {
        movieList = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return movieList.size();
        if (movieList != null)
            return movieList.size();
        return 0;
    }

}
