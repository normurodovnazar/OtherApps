package com.normurodov_nazar.sample;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUniversityService {
    @GET("/search?country=United+States")
    Call<List<University>> getUniversities();

    @GET("/search")
    Call<List<University>> getUniversities(@Query("name") String name);
}
