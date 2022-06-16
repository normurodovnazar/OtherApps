package com.normurodov_nazar.otherapps.Customizations;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.normurodov_nazar.otherapps.Listeners.SuccessListener;
import com.normurodov_nazar.otherapps.Models.Music;
import com.normurodov_nazar.otherapps.Models.Paths;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Hey {
    public static void print(String a, String s) {
        Log.e(a, s);
    }

    public static void showEditDialog(Context context, SuccessListener successListener){
        EditFieldDialog dialog = new EditFieldDialog(context,successListener);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static ArrayList<Music> getAllSongs(Context context) {
        ArrayList<Music> musicArrayList = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        //MediaStore.Audio.Media.
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            musicArrayList.add(new Music( cursor.getInt(5),cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4)));
        }
        cursor.close();
        return musicArrayList;
    }

    public static ArrayList<Music> getMusicInFolder(Context context, File file) {
        ArrayList<Music> allMusic = getAllSongs(context), sortedMusic = new ArrayList<>();
        for (Music m : allMusic) {
            File f = new File(m.getPath());
            if (f.getParent() != null)
                if (f.getParent().equals(file.getPath())) {
                    sortedMusic.add(m);
                }
        }
        return sortedMusic;
    }

    public static ArrayList<File> getFoldersWithMusic(Context context) {
        ArrayList<Music> musicList = getAllSongs(context);
        ArrayList<File> musicFolders = new ArrayList<>();
        for (Music m : musicList) {
            File file = new File(m.getPath());
            if (!AContainsB(musicFolders, file)) {
                musicFolders.add(file.getParentFile());
            }
        }
        return musicFolders;
    }

    public static boolean AContainsB(ArrayList<File> musicFolders, File m) {
        boolean contains = false;
        for (File mFile : musicFolders) {
            if (mFile.getPath().equals(m.getParent())) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static String getDuration(String millis){
        int time = Integer.parseInt(millis)/1000;
        int seconds = time%60,minutes = (time-seconds)/60;
        return format(minutes)+":"+format(seconds);
    }
    public static String getDuration(int millis){
        int time = millis/1000;
        int seconds = time%60,minutes = (time-seconds)/60;
        return format(minutes)+":"+format(seconds);
    }

    public static String format(int i){
        String s = String.valueOf(i);
        return s.length()==1 ? "0"+s : s;
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("a",Context.MODE_PRIVATE);
    }

    public static ArrayList<Music> getMusicsFromPaths(Context context,List<Paths> musicPaths) {
        ArrayList<Music> musics = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };
        for (Paths path : musicPaths){
            Cursor cursor = context.getContentResolver().query(uri,projection,MediaStore.Audio.Media.DATA + " = ?",new String[]{path.getPath()},null);
            if (cursor!=null) {
                if (cursor.getCount()>0) {
                    if (cursor.moveToFirst()) {
                        musics.add(new Music( cursor.getInt(5),cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4)));
                    }
                }
                cursor.close();
            }
        }
        return musics;
    }
}
