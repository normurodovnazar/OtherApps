package com.normurodov_nazar.otherapps.Sources;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.normurodov_nazar.otherapps.Models.Music;

import java.io.File;
import java.util.ArrayList;

public class Hey {
    public static void print(String a,String s){
        Log.e(a,s);
    }

    public static ArrayList<Music> getAllSongs(Context context){
        ArrayList<Music> musicArrayList = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
        };
        //MediaStore.Audio.Media.
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        while (cursor.moveToNext()){
            musicArrayList.add(new Music(cursor.getString(1),cursor.getString(2), cursor.getString(4)));
        }
        cursor.close();
        return musicArrayList;
    }

    public static ArrayList<File> getFoldersWithMusic(Context context){
        ArrayList<Music> musicList = getAllSongs(context);
        ArrayList<File> musicFolders = new ArrayList<>();
        for (Music m : musicList){
            File file = new File(m.getPath());
            if (!AContainsB(musicFolders,file)){
                musicFolders.add(file.getParentFile());
            }
        }
        return musicFolders;
    }

    public static boolean AContainsB(ArrayList<File> musicFolders,File m){
        boolean contains = false;
        for (File mFile : musicFolders){
            if (mFile.getPath().equals(m.getParent())) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
