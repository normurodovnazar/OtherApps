package com.normurodov_nazar.sample;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class University {

    @SerializedName("name")
    private final String name;

    @SerializedName("country")
    private final String country;

    @SerializedName("state-province")
    private final String stateOrProvince;

    @SerializedName("web_pages")
    private final String[] webPages;

    @NonNull
    @Override
    public String toString() {
        return "University{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", stateOrProvince='" + stateOrProvince + '\'' +
                ", webPages=" + Arrays.toString(webPages) +
                '}';
    }

    public String getUniversityName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public String[] getWebPages() {
        return webPages;
    }

    public University(String name, String country, String stateOrProvince, String[] webPages) {
        this.name = name;
        this.country = country;
        this.stateOrProvince = stateOrProvince;
        this.webPages = webPages;
    }
}
