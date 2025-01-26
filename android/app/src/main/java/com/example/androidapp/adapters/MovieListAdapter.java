package com.example.androidapp.adapters;

import com.bumptech.glide.Glide;
import com.example.androidapp.entities.Movie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private final LayoutInflater mInfalter;
    private List<Movie> movies;

    public MovieListAdapter(Context context) {
        mInfalter = LayoutInflater.from(context);
    }
    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        private MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.iv_movie);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
//        if (movieList != null) {
        if (movies != null && !movies.isEmpty()) {
            final Movie currentMovie = movies.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(currentMovie.getImage())
                    .error(R.drawable.default_image)
                    .into(holder.movieImage);
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
//        return movieList.size();
        if (movies != null)
            return movies.size();
        return 0;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    //chat
//    private Context context;
//    private List<Movie> movies;
//
//    public MovieAdapter(Context context, List<Movie> movies) {
//        this.context = context;
//        this.movies = movies;
//    }
//
//    @NonNull
//    @Override
//    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false);
//        return new MovieViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
//        Movie movie = movies.get(position);
//        Glide.with(context)
//                .load(movie.getImage())
//                .centerCrop()  // אם תרצה לשמור על יחס התמונה ולחתוך את הקצוות
//                .into(holder.movieImage);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return movies.size();
//    }
//
//    public static class MovieViewHolder extends RecyclerView.ViewHolder {
//        ImageView movieImage;
//
//        public MovieViewHolder(View itemView) {
//            super(itemView);
//            movieImage = itemView.findViewById(R.id.iv_movie);
//        }
//    }

}
