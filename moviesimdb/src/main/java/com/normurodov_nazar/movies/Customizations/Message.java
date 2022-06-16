package com.normurodov_nazar.movies.Customizations;

import retrofit2.Response;

public class Message {
    Response<MovieList> movieList;
    String error = null;
    Type type;
    Response<MovieDescriptionResult> description;

    public Message(Type type, Response<MovieList> movieList,String a){
        this.type = type;
        this.movieList = movieList;
    }

    public Message(Type type,String error){
        this.type = type;
        this.error = error;
    }

    public Message(Type type){
        this.type = type;
    }

    public Message(Type type, Response<MovieDescriptionResult> response){
        this.type = type;
        this.description = response;
    }

    public Response<MovieDescriptionResult> getDescription() {
        return description;
    }

    public Response<MovieList> getMovieList() {
        return movieList;
    }

    public String getError() {
        return error;
    }

    public Type getType() {
        return type;
    }
}
