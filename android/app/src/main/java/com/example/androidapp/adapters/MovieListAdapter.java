package com.example.androidapp.adapters;

import com.bumptech.glide.Glide;
//import com.example.androidapp.MovieActivity;
import com.example.androidapp.entities.Movie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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
        ImageButton movieImage;
        private MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageBtnMovie);
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.movie_layout, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (movies != null) {
            final Movie current = movies.get(position);
            Log.d("MovieListAtapter", current.getImage());
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2:12345/" + current.getImage())
                    .into(holder.movieImage);

            holder.movieImage.setTag(current);

//            holder.movieImage.setOnClickListener(v -> {
//                Movie movie = (Movie) v.getTag();
//                Intent intent = new Intent(v.getContext(), MovieActivity.class);
//                intent.putExtra("movie", movie.getId());
//                v.getContext().startActivity(intent);
//            });
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


}
