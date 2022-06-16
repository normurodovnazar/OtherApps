package com.normurodov_nazar.otherapps.Models;

public class Music {
    public final int id;


    String artist;

    String title;

    final String path;

    final String duration;

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    String album;

    public String getArtist() {
        return artist;
    }

    public Music(int id,String title, String path, String duration, String album, String artist) {
        this.id = id;
        this.artist = artist;
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
