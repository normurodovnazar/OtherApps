package com.normurodov_nazar.contactsapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    List<Contact> getAllContacts();

    @Insert
    void addContacts(Contact... contacts);

    @Delete
    void delete(Contact contact);
}
