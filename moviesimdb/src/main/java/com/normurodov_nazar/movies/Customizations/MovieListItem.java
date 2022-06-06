package com.normurodov_nazar.movies.Customizations;

import com.google.gson.annotations.SerializedName;

public class MovieListItem {
    @SerializedName("Title")
    final String title;
    @SerializedName("Year")
    final String year;
    @SerializedName("imdbID")
    final String id;
    @SerializedName("Type")
    final String type;
    @SerializedName("Poster")
    final String imageUrl;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public MovieListItem(String title, String year, String id, String type, String imageUrl) {
        this.title = title;
        this.year = year;
        this.id = id;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "MovieListItem{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
