package com.normurodov_nazar.movies.Customizations;

import static com.normurodov_nazar.movies.Sources.Hey.print;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.normurodov_nazar.movies.R;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieHolder>{
    final Context context;
    final List<MovieListItem> movies;
    final ClickListener clickListener;

    public MoviesAdapter(Context context, List<MovieListItem> movies, ClickListener clickListener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MovieHolder(LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        print(movies.get(position).title,movies.get(position).toString());
        holder.setMovie(movies.get(position),clickListener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder{
        final TextView title,type,year;
        final ImageView image;
        final Context context;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.title);
            type = itemView.findViewById(R.id.type);
            year = itemView.findViewById(R.id.year);
            image = itemView.findViewById(R.id.movieImage);
        }

        public void setMovie(MovieListItem movie,ClickListener clickListener ){
            title.setText(movie.getTitle());
            type.setText(movie.getType());
            year.setText(movie.getYear());
            if (!"N/A".equals(movie.getImageUrl()) && movie.getImageUrl()!=null) Glide.with(context).load(movie.getImageUrl()).placeholder(R.drawable.ic_movie).into(image);
            itemView.setOnClickListener(c-> clickListener.onMovieClick(movie.getId()));
        }
    }
}
