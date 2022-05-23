package com.normurodov_nazar.otherapps.Sources;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.GnssAntennaInfo;
import android.location.GpsStatus;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.normurodov_nazar.otherapps.Models.Music;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.transform.ErrorListener;

public class Hey {
    public static void print(String a, String s) {
        Log.e(a, s);
    }

    public static ArrayList<Music> getAllSongs(Context context) {
        ArrayList<Music> musicArrayList = new ArrayList<>();
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
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
            musicArrayList.add(new Music(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
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

    public static void startAnimation(View view){
        view.clearAnimation();
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setDuration(800);
        view.startAnimation(alphaAnimation);
    }

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("a",Context.MODE_PRIVATE);
    }
}
