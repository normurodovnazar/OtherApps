package com.normurodov_nazar.otherapps.Models;

public class Music {
    final String title;
    final String path;
    final String duration;
    final String album;

    public Music(String title, String path, String duration, String album) {
        this.title = title;
        this.path = path;
        this.duration = duration;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public String getDuration() {
        return duration;
    }
}
