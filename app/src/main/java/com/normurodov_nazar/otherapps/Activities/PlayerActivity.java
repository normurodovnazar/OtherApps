package com.normurodov_nazar.otherapps.Activities;

import static com.normurodov_nazar.otherapps.Sources.Hey.getDuration;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;
import static com.normurodov_nazar.otherapps.Sources.PublicData.inOrder;
import static com.normurodov_nazar.otherapps.Sources.PublicData.playMode;
import static com.normurodov_nazar.otherapps.Sources.PublicData.repeat;
import static com.normurodov_nazar.otherapps.Sources.PublicData.shuffle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.normurodov_nazar.otherapps.R;
import com.normurodov_nazar.otherapps.Sources.Hey;
import com.normurodov_nazar.otherapps.Sources.Mode;
import com.normurodov_nazar.otherapps.databinding.ActivityPlayerBinding;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity {

    ActivityPlayerBinding b;
    MediaPlayer player;
    SharedPreferences preferences;
    Mode mode = Mode.inOrder;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        if (player!=null){
            player.release();
        }
        File file = new File(currentMusic.getPath());
        if (file.exists()){
            player = MediaPlayer.create(this,Uri.fromFile(file));
            player.start();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (player.isPlaying()) b.progress.setText(getDuration(player.getCurrentPosition())+" / "+getDuration(player.getDuration()));
                }
            },0,1000);
            player.setOnCompletionListener(mediaPlayer -> {
                switch (mode){
                    case inOrder:
                        //playNext
                        break;
                    case shuffle:
                        //shuffle
                        break;
                }
            });
            b.play.setOnClickListener(view -> {
                if (player.isPlaying()) {
                    player.pause();
                    b.play.setImageResource(R.drawable.ic_play);
                }else {
                    player.start();
                    b.play.setImageResource(R.drawable.ic_pause);
                }
            });
            b.replay10.setOnClickListener(d->{
                int p = player.getCurrentPosition()-10000;
                player.seekTo(Math.max(p, 0));
            });
        }else Toast.makeText(this, "not exists", Toast.LENGTH_SHORT).show();
        preferences = Hey.getSharedPreferences(this);
        applyMode();
        if (player.isPlaying()) b.play.setImageResource(R.drawable.ic_pause);
    }

    private void applyMode() {
        switch (preferences.getString(playMode, inOrder)){
            case shuffle:
                mode = Mode.shuffle;
                break;
            case repeat:
                mode = Mode.repeat;
                break;
            case inOrder:
                mode = Mode.inOrder;
                break;
        }
        player.setLooping(mode==Mode.repeat);
    }
}