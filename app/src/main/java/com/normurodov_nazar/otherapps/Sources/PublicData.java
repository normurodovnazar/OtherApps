package com.normurodov_nazar.otherapps.Sources;

import android.media.MediaPlayer;

import com.normurodov_nazar.otherapps.Models.Music;

import java.util.ArrayList;

public class PublicData {
    public static final String shuffle = "shuffle",repeat = "repeat", inOrder = "order",playMode = "playMode",autoPlayAfterClose = "autoPlayAfterClose",autoPauseAfterFar = "autoPauseAfterFar",autoPlayOnClick = "autoPlayOnClick",night = "night",day = "day",system = "system",mode = "mode";
    public static Music currentMusic;
    public static ArrayList<Music> currentFolder;
    public static int currentPosition;
    public static MediaPlayer player;
}
