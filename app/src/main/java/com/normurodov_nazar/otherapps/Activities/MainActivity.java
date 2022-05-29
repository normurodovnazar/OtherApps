package com.normurodov_nazar.otherapps.Activities;

import static com.normurodov_nazar.otherapps.Customizations.Hey.getFoldersWithMusic;
import static com.normurodov_nazar.otherapps.Customizations.Hey.getMusicInFolder;
import static com.normurodov_nazar.otherapps.Customizations.Hey.print;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentFolder;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentPosition;
import static com.normurodov_nazar.otherapps.Sources.PublicData.day;
import static com.normurodov_nazar.otherapps.Sources.PublicData.mode;
import static com.normurodov_nazar.otherapps.Sources.PublicData.night;
import static com.normurodov_nazar.otherapps.Sources.PublicData.system;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.normurodov_nazar.otherapps.Customizations.FolderAdapter;
import com.normurodov_nazar.otherapps.Customizations.Hey;
import com.normurodov_nazar.otherapps.Customizations.MusicAdapter;
import com.normurodov_nazar.otherapps.Models.Music;
import com.normurodov_nazar.otherapps.Models.Paths;
import com.normurodov_nazar.otherapps.R;

import com.normurodov_nazar.otherapps.Sources.MyDatabase;
import com.normurodov_nazar.otherapps.Sources.PathDao;
import com.normurodov_nazar.otherapps.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    boolean isFolderView = true;
    ActivityResultLauncher<Intent> launcher;
    MusicAdapter adapter;
    ActivityMainBinding b;
    SharedPreferences preferences;
    PathDao pathDao;
    MyDatabase database;

    Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what==0){
                isFolderView = false;
                gotoFav();
            }else Toast.makeText(MainActivity.this, R.string.removedLater, Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (adapter!=null) adapter.updateItem();
        });
        setContentView(b.getRoot());
        preferences = Hey.getSharedPreferences(this);
        print(mode,preferences.getString(mode,system));
        switch (preferences.getString(mode,system)){
            case day:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case night:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case system:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            showFolders();
        } else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        b.playlist.setOnClickListener(c->{
            database = Room.databaseBuilder(this,MyDatabase.class,"f").build();
            pathDao = database.getDao();
            Thread thread = new Thread(new TimerTask() {
                @Override
                public void run() {
                    List<Paths> musicPaths = pathDao.getAll();
                    currentFolder = Hey.getMusicsFromPaths(MainActivity.this,musicPaths);
                    print("S", String.valueOf(musicPaths.size()));
                    handler.sendEmptyMessage(0);
                }
            });
            thread.start();
        });
        b.night.setOnClickListener(c->{
            preferences.edit().putString(mode,night).apply();
            recreate();
        });
        b.day.setOnClickListener(c->{
            preferences.edit().putString(mode,day).apply();
            recreate();
        });
        b.auto.setOnClickListener(c->{
            preferences.edit().putString(mode,system).apply();
            recreate();
        });
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    @Override
    public void onBackPressed() {
        if (isFolderView) super.onBackPressed(); else {
            showFolders();
            b.topPanel.setVisibility(View.VISIBLE);
            isFolderView = true;
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
        b.topPanel.setVisibility(View.GONE);
        ArrayList<Music> musics = getMusicInFolder(this, file);
        adapter = new MusicAdapter(this, musics, (position, data) -> {
            currentMusic = (Music) data;
            currentPosition = position;
            currentFolder = musics;
            launcher.launch(new Intent(this, PlayerActivity.class));
        }, (position, data) -> {

        }, false);
        b.recycler.setAdapter(adapter);
    }
    void gotoFav(){
        b.topPanel.setVisibility(View.GONE);
        adapter = new MusicAdapter(this, currentFolder, (position, data) -> {
            currentMusic = (Music) data;
            currentPosition = position;
            launcher.launch(new Intent(this, PlayerActivity.class));
        }, (position, data) -> {
            Thread thread = new Thread(new TimerTask() {
                @Override
                public void run() {
                    pathDao.delete((Paths) data);
                    handler.sendEmptyMessage(1);
                }
            });
            thread.start();
        }, true);
        b.recycler.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED) showFolders(); else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }
}