package com.example.androidapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidapp.AppContext;
import com.example.androidapp.R;
import com.example.androidapp.entities.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private OnMovieSelectListener listener;
    private Context context;


//    public MovieAdapter(Context context, List<Movie> movies, OnMovieClickListener listener) {
//        this.movies = movies;
//        this.context = context;
//        this.onMovieClickListener = listener;
//    }
    public MovieAdapter(Context context,List<Movie> movies, OnMovieSelectListener listener) {
        this.movies = movies;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.context)
                .inflate(R.layout.movie_layout, parent, false);  // אתה יכול לשנות את ה-XML של item_movie
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Log.e("MovieViewModel", "Movies: " + movie.getImage());
        Log.e("MovieViewModel", "Movies: " + movies.toString());

//        holder.movieTitle.setText(movie.getTitle());
        if (movies != null && !movies.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2:12345/" + movie.getImage())
                    .into(holder.moviePoster);

        } else {
            Log.e("MovieViewModel", "No movies available or response is null");
        }
//        holder.checkBox.setChecked(movie.isSelected());
//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            listener.onMovieSelect(movie, isChecked);
//        });
    }

    public int getItemCount() {
        return movies.size();
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
    public interface OnMovieSelectListener {
        void onMovieSelect(Movie movie, boolean isSelected);
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
            moviePoster = itemView.findViewById(R.id.iv_movie);
        }
    }

}
