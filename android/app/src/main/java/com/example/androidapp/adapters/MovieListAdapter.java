package com.example.androidapp.adapters;
import com.bumptech.glide.Glide;
import com.example.androidapp.activities.SelectedCategoryActivity;
import com.example.androidapp.entities.Movie;
import android.content.Context;
import com.example.androidapp.activities.HomeActivity;
import com.example.androidapp.activities.MovieActivity;
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
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2:12345/api/" + current.getImage())
                    .into(holder.movieImage);

            holder.movieImage.setTag(current);

            Context context = holder.itemView.getContext();
            holder.movieImage.setOnClickListener(v -> {
                if (context instanceof HomeActivity || context instanceof SelectedCategoryActivity) {
                    Intent intent = new Intent(v.getContext(), MovieActivity.class);
                    intent.putExtra("id", current.get_id());
                    intent.putExtra("name", current.getName());
                    intent.putExtra("movie_time", current.getMovie_time());
                    intent.putExtra("image", current.getImage());
                    intent.putExtra("Publication_year", current.getPublication_year());
                    intent.putExtra("description", current.getDescription());
                    intent.putExtra("age", current.getAge());
                    intent.putExtra("video", current.getVideo());
                    intent.putExtra("trailer", current.getTrailer());


                    v.getContext().startActivity(intent);
                } else {
                    Log.d("MovieListAdapter", "did not work");
                }
            });
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (movies != null)
            return movies.size();
        return 0;
    }

    public List<Movie> getMovies() {
        return movies;
    }

}
