package com.example.androidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidapp.R;
import com.example.androidapp.entities.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private List<String> selectedMovieIds = new ArrayList<>();

    private Context context;

    // Constructor to initialize the adapter with context and a list of movies
    public MovieAdapter(Context context,List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    // Create a new ViewHolder for the RecyclerView item (called when a new item view is needed)
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.context)
                .inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(itemView);
    }

    // Bind the data (movie) to the corresponding ViewHolder
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        if (movies != null && !movies.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2:12345/api/" + movie.getImage())
                    .centerCrop()
                    .into(holder.moviePoster);

        } else {
            Log.e("MovieViewModel", "No movies available or response is null");
        }

        // Set an OnClickListener for the movie poster to toggle selection state
        holder.moviePoster.setOnClickListener(v -> {
            String movieId = movie.get_id();
            if (selectedMovieIds.contains(movieId)) {
                selectedMovieIds.remove(movieId);
            } else {
                selectedMovieIds.add(movieId);
                Log.d("MovieAdapter", "Selected movie ID: " + movieId);
            }

        });
    }

    // Get the total number of movies to display
    public int getItemCount() {
        return movies.size();
    }

    // Get the list of selected movie IDs
    public List<String> getSelectedMovieIds() {
        return selectedMovieIds;
    }

    // Update the list of movies and refresh the RecyclerView
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    // ViewHolder class to represent each movie item view
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvYear,tvTime, tvDescription;
        ImageButton moviePoster;
        VideoView videoView;

        // Constructor to initialize the views
        public MovieViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.movieTitle);
            tvYear = itemView.findViewById(R.id.year);
            tvDescription = itemView.findViewById(R.id.movieDescription);
            tvTime = itemView.findViewById(R.id.movieTime);
            videoView = itemView.findViewById(R.id.videoView);
            moviePoster = itemView.findViewById(R.id.imageBtnMovie);
        }
    }

}
