package com.normurodov_nazar.otherapps.Sources;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.normurodov_nazar.otherapps.Models.Paths;

@Database(entities = {Paths.class},version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract PathDao getDao();
}
