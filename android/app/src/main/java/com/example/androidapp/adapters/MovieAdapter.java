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
//    private OnMovieSelectListener listener;
    private List<String> selectedMovieIds = new ArrayList<>();

    private Context context;

    public MovieAdapter(Context context,List<Movie> movies) {
        this.movies = movies;
//        this.listener = listener;
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
        Log.d("MovieViewModel", "Movies: " + movie.getImage());
        Log.d("MovieViewModel", "Movies: " + movies.toString());

//        holder.movieTitle.setText(movie.getTitle());
        if (movies != null && !movies.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2:12345/api/" + movie.getImage())
                    .centerCrop()
                    .into(holder.moviePoster);

        } else {
            Log.e("MovieViewModel", "No movies available or response is null");
        }

        holder.moviePoster.setOnClickListener(v -> {
            String movieId = movie.getId();
            Log.d("MovieAdapter", "movie ID: " + movieId);
            if (selectedMovieIds.contains(movieId)) {
                selectedMovieIds.remove(movieId);
                Log.d("MovieAdapter", "Deselected movie ID: " + movieId);
            } else {
                selectedMovieIds.add(movieId);
                Log.d("MovieAdapter", "Selected movie ID: " + movieId);
            }

//            listener.onMovieSelect(movie, selectedMovieIds.contains(movieId));
        });
    }

    public int getItemCount() {
        return movies.size();
    }
    public List<String> getSelectedMovieIds() {
        return selectedMovieIds;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
//    public interface OnMovieSelectListener {
//        void onMovieSelect(Movie movie, boolean isSelected);
//    }
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvYear,tvTime, tvDescription;
        ImageButton moviePoster;
        VideoView videoView;

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
