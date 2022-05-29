package com.normurodov_nazar.otherapps.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Paths {
    @PrimaryKey
    public final int id;
    @ColumnInfo(name = "path")
    final String path;

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public Paths(int id, String path) {
        this.id = id;
        this.path = path;
    }
}