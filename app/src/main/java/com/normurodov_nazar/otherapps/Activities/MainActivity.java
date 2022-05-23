package com.normurodov_nazar.otherapps.Activities;

import static com.normurodov_nazar.otherapps.Sources.Hey.getFoldersWithMusic;
import static com.normurodov_nazar.otherapps.Sources.Hey.getMusicInFolder;
import static com.normurodov_nazar.otherapps.Sources.Hey.print;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.normurodov_nazar.otherapps.Listeners.ItemCLickListener;
import com.normurodov_nazar.otherapps.Models.Music;
import com.normurodov_nazar.otherapps.Sources.FolderAdapter;
import com.normurodov_nazar.otherapps.Sources.MusicAdapter;
import com.normurodov_nazar.otherapps.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean isFolderView = true;

    ActivityMainBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            showFolders();
        } else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isFolderView) super.onBackPressed(); else {
            showFolders();
        }
    }

    void showFolders(){
        ArrayList<File> files = getFoldersWithMusic(this);
        for (File f : files) print("p",f.getPath());
        FolderAdapter adapter = new FolderAdapter(this, files, (position, data) -> {
            isFolderView = false;
            File file = (File) data;
            gotoFolder(file);
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);
    }

    void gotoFolder(File file){
        MusicAdapter adapter = new MusicAdapter(this, getMusicInFolder(this, file), (position, data) -> {
            currentMusic = (Music) data;
            startActivity(new Intent(this,PlayerActivity.class));
        });
        b.recycler.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED) showFolders(); else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }
}