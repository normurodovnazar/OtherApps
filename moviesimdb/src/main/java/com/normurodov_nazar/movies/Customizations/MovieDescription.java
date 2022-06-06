package com.normurodov_nazar.movies.Customizations;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class MovieDescription {
    @SerializedName("Title")
    final String title;
    @SerializedName("Released")
    final String released;
    @SerializedName("Poster")
    final String imageUrl;
    @SerializedName("Runtime")
    final String runtime;
    @SerializedName("Genre")
    final String genre;
    @SerializedName("Director")
    final String director;
    @SerializedName("Writer")
    final String writer;
    @SerializedName("Actors")
    final String actors;
    @SerializedName("Plot")
    final String detials;
    @SerializedName("Language")
    final String language;
    @SerializedName("Country")
    final String country;
    @SerializedName("Awards")
    final String awards;
    @SerializedName("Production")
    final String production;
    @SerializedName("Website")
    final String website;
    @SerializedName("Type")
    final String type;
    @SerializedName("imdbRating")
    final String rating;
    @SerializedName("imdbVotes")
    final String votes;
    @SerializedName("BoxOffice")
    final String money;
    @SerializedName("Response")
    final boolean response;


    public MovieDescription(String title, String released, String imageUrl, String runtime, String genre, String director, String writer, String actors, String plot, String language, String country, String awards, String production, String website, String type, String rating, String votes, String money, boolean response) {
        this.title = title;
        this.released = released;
        this.imageUrl = imageUrl;
        this.runtime = runtime;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.detials = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.production = production;
        this.website = website;
        this.type = type;
        this.rating = rating;
        this.votes = votes;
        this.money = money;
        this.response = response;
    }

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getWriter() {
        return writer;
    }

    public String getActors() {
        return actors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetials() {
        return detials;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getAwards() {
        return awards;
    }

    public String getProduction() {
        return production;
    }

    public String getWebsite() {
        return website;
    }

    public String getType() {
        return type;
    }

    public String getRating() {
        return rating;
    }

    public String getVotes() {
        return votes;
    }

    public String getMoney() {
        return money;
    }

    public boolean isResponse() {
        return response;
    }

    @NonNull
    @Override
    public String toString() {
        return "MovieDescription{" +
                "title='" + title + '\'' +
                ", released='" + released + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", writer='" + writer + '\'' +
                ", actors='" + actors + '\'' +
                ", detials='" + detials + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", awards='" + awards + '\'' +
                ", production='" + production + '\'' +
                ", website='" + website + '\'' +
                ", type='" + type + '\'' +
                ", rating='" + rating + '\'' +
                ", votes='" + votes + '\'' +
                ", money='" + money + '\'' +
                ", response=" + response +
                '}';
    }
}
