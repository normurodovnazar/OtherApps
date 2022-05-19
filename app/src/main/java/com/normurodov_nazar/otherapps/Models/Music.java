package com.normurodov_nazar.otherapps.Models;

public class Music {
    final String title;
    final String path;
    final String duration;

    public Music(String title, String path, String duration) {
        this.title = title;
        this.path = path;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }
}
