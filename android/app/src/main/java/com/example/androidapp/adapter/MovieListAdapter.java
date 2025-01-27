package com.example.androidapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidapp.Movie;
import com.example.androidapp.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private List<Movie> recommendedMovies;
    private Context context;
    private OnMovieClickListener onMovieClickListener;


    public MovieListAdapter(Context context,List<Movie> recommendedMovies, OnMovieClickListener listener) {
        this.recommendedMovies = recommendedMovies;
        this.context = context;
        this.onMovieClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.context)
                .inflate(R.layout.movie_layout, parent, false);  // אתה יכול לשנות את ה-XML של item_movie
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = recommendedMovies.get(position);
//        holder.movieTitle.setText(movie.getTitle());
        Glide.with(holder.moviePoster.getContext())
                .load("http://10.0.2.2:12345/" + movie.getImage())
                .into(holder.moviePoster);
        holder.itemView.setOnClickListener(v -> {
            onMovieClickListener.onMovieClick(movie);
        });
    }

    public int getItemCount() {
        return recommendedMovies.size();
    }


    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvYear,tvTime, tvDescription;
        ImageView moviePoster;
        VideoView videoView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.movieTitle);
            tvYear = itemView.findViewById(R.id.year);
            tvDescription = itemView.findViewById(R.id.movieDescription);
            tvTime = itemView.findViewById(R.id.movieTime);
            videoView = itemView.findViewById(R.id.videoView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
        }
    }

}
