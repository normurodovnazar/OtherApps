package com.normurodov_nazar.movies.Customizations;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MovieRequest {

    @Headers({
            "content-type: application/json",
            "authorization: apikey 5NsAujtpa9zHpciunl775O:25NAT4MbEXlOSmvRIVAv8s"
    })
    @GET("imdbSearchByName")
    Call<MovieList> getMoviesByTitle(@Query("query") String text);

    @Headers({
            "content-type: application/json",
            "authorization: apikey 5NsAujtpa9zHpciunl775O:25NAT4MbEXlOSmvRIVAv8s"
    })
    @GET("imdbSearchById")
    Call<MovieDescriptionResult> getMovieById(@Query("movieId") String id);
}
