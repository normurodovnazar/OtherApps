package com.normurodov_nazar.otherapps.Sources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.normurodov_nazar.otherapps.Models.Paths;

import java.util.List;

@Dao
public interface PathDao {
    @Query("SELECT * FROM paths")
    List<Paths> getAll();

    @Insert
    void insertAll(Paths path);

    @Delete
    void delete(Paths path);
}
