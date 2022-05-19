package com.normurodov_nazar.otherapps;

import static com.normurodov_nazar.otherapps.Sources.Hey.getFoldersWithMusic;
import static com.normurodov_nazar.otherapps.Sources.Hey.print;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.normurodov_nazar.otherapps.Listeners.ItemCLickListener;
import com.normurodov_nazar.otherapps.Sources.FolderAdapter;
import com.normurodov_nazar.otherapps.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

    void showFolders(){
        ArrayList<File> files = getFoldersWithMusic(this);
        for (File f : files) print("p",f.getPath());
        FolderAdapter adapter = new FolderAdapter(this, files, (position, data) -> {
            File file = (File) data;
            File[] files1 = file.listFiles();
            print(String.valueOf(files1.length)," DSF");
        });
        b.recycler.setLayoutManager(new LinearLayoutManager(this));
        b.recycler.setAdapter(adapter);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED) showFolders(); else requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
    }
}