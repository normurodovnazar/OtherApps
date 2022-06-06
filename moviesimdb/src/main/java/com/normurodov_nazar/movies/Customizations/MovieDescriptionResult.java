package com.normurodov_nazar.movies.Customizations;

import com.google.gson.annotations.SerializedName;

public class MovieDescriptionResult {
    @SerializedName("success")
    final boolean success;
    @SerializedName("result")
    final MovieDescription result;

    public MovieDescriptionResult(boolean success, MovieDescription result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public MovieDescription getResult() {
        return result;
    }
}
