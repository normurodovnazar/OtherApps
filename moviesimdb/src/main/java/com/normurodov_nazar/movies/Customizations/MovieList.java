package com.normurodov_nazar.movies.Customizations;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    @SerializedName("success")
    final boolean success;

    public boolean isSuccess() {
        return success;
    }

    public List<MovieListItem> getMovieListItems() {
        return movieListItems;
    }

    @SerializedName("result")
    final List<MovieListItem> movieListItems;

    public MovieList(boolean success, List<MovieListItem> movieListItems) {
        this.success = success;
        this.movieListItems = movieListItems;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "success=" + success +
                ", movieListItems=" + movieListItems +
                '}';
    }
}
