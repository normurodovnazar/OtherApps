package com.normurodov_nazar.contactsapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.normurodov_nazar.contactsapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ContactDatabase database;
    ContactDao dao;
    List<Contact> contacts;
    MAdapter adapter;
    Contact contact;

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        database = Room.databaseBuilder(this, ContactDatabase.class, "c").allowMainThreadQueries().build();

        dao = database.contactDao();
        contacts = dao.getAllContacts();
        adapter = new MAdapter(this, contacts, contact -> {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                this.contact = contact;
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 0);
            } else {
                // else block means user has already accepted.And make your phone call here.
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getNumber())));
            }
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);
        b.newContact.setOnClickListener(c -> {
            showDialog();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getNumber())));
        }
    }

    void showDialog() {
        NewContactDialog dialog = new NewContactDialog(this, contact -> {
            dao.addContacts(contact);
            recreate();
        });
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}