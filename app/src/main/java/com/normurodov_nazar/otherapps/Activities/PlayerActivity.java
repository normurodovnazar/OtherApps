package com.normurodov_nazar.otherapps.Activities;

import static com.normurodov_nazar.otherapps.Customizations.Hey.getDuration;
import static com.normurodov_nazar.otherapps.Customizations.Hey.print;
import static com.normurodov_nazar.otherapps.Customizations.Hey.showEditDialog;
import static com.normurodov_nazar.otherapps.Sources.PublicData.autoPauseAfterFar;
import static com.normurodov_nazar.otherapps.Sources.PublicData.autoPlayAfterClose;
import static com.normurodov_nazar.otherapps.Sources.PublicData.autoPlayOnClick;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentFolder;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentMusic;
import static com.normurodov_nazar.otherapps.Sources.PublicData.currentPosition;
import static com.normurodov_nazar.otherapps.Sources.PublicData.inOrder;
import static com.normurodov_nazar.otherapps.Sources.PublicData.playMode;
import static com.normurodov_nazar.otherapps.Sources.PublicData.player;
import static com.normurodov_nazar.otherapps.Sources.PublicData.repeat;
import static com.normurodov_nazar.otherapps.Sources.PublicData.shuffle;

import android.Manifest;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.normurodov_nazar.otherapps.Customizations.Hey;
import com.normurodov_nazar.otherapps.Models.Paths;
import com.normurodov_nazar.otherapps.R;
import com.normurodov_nazar.otherapps.Sources.Mode;

import com.normurodov_nazar.otherapps.Sources.MyDatabase;
import com.normurodov_nazar.otherapps.Sources.PathDao;
import com.normurodov_nazar.otherapps.databinding.ActivityPlayerBinding;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity implements SensorEventListener {

    ActivityPlayerBinding b;
    SharedPreferences preferences;
    Mode mode = Mode.inOrder;
    Timer timer;
    File file;
    int position = -1;
    SensorManager sensorManager;
    Sensor sensor;
    boolean autoPauseFar, autoPlayClose, autoStart;
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;
    Handler handler,toast;
    float sensorValue = 1f;
    PathDao pathDao;
    MyDatabase database;
    AudioManager audioManager;
    Mp3File mp3File = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        preferences = Hey.getSharedPreferences(this);
        initVars();
        getPlaySettings();
        setListeners();
        playSpeaker(true, autoStart);

    }

    private void initVars() {
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        database = Room.databaseBuilder(this,MyDatabase.class,"f").build();
        pathDao = database.getDao();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "myApp:myWakeLock");
        handler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                b.progress.setText(msg.getData().getString("a"));
                if (player != null) {
                    if (player.isPlaying()) b.play.setImageResource(R.drawable.ic_pause);
                    else b.play.setImageResource(R.drawable.ic_play);
                }
            }
        };
        toast = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what==3) Toast.makeText(PlayerActivity.this, R.string.added, Toast.LENGTH_SHORT).show();
                else {
                    String e = msg.getData().getString("e");
                    if (e.equals("UNIQUE constraint failed: Music.id (code 1555)")) Toast.makeText(PlayerActivity.this, R.string.added, Toast.LENGTH_SHORT).show(); else
                        Toast.makeText(PlayerActivity.this, e, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void getPlaySettings() {
        autoPauseFar = preferences.getBoolean(autoPauseAfterFar, false);
        b.autoPauseWhenFarFromEar.setChecked(autoPauseFar);
        autoPlayClose = preferences.getBoolean(autoPlayAfterClose, true);
        b.autoPlayWhenCloseToEar.setChecked(autoPlayClose);
        autoStart = preferences.getBoolean(autoPlayOnClick, true);
        b.autoPlayWhenClickMusic.setChecked(autoStart);
    }

    private void setListeners() {
        b.autoPauseWhenFarFromEar.setOnClickListener(c -> {
            preferences.edit().putBoolean(autoPauseAfterFar, !autoPauseFar).apply();
            autoPauseFar = !autoPauseFar;
        });
        b.autoPlayWhenCloseToEar.setOnClickListener(c -> {
            preferences.edit().putBoolean(autoPlayAfterClose, !autoPlayClose).apply();
            autoPauseFar = !autoPauseFar;
        });
        b.autoPlayWhenClickMusic.setOnClickListener(c -> {
            preferences.edit().putBoolean(autoPlayOnClick, !autoStart).apply();
            autoStart = !autoStart;
        });
        b.editSongInfo.setOnClickListener(c->{
            if (permissionGranted()) editSongInfo();  else requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        });
        b.addToPlaylist.setOnClickListener(c->{
            Thread thread = new Thread(new TimerTask() {
                @Override
                public void run() {
                    try {
                        pathDao.insertAll(new Paths(currentMusic.id,currentMusic.getPath()));
                        toast.sendEmptyMessage(3);
                    }catch (Exception e){
                        Message message = new Message();
                        message.what = -1;
                        Bundle bundle = new Bundle();bundle.putString("e",e.getLocalizedMessage());
                        message.setData(bundle);
                        toast.sendMessage(message);
                    }
                }
            });
            thread.start();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
            editSongInfo();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        print("lifecycle","onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        print("lifecycle","onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        print("lifecycle","onResume");
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        print("lifecycle","onPause");
        sensorManager.unregisterListener(this);
        if (wakeLock.isHeld()) wakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        print("lifecycle","onDestroy");
        timer.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            if (sensorValue==0f){
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
            }else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
            }
            return true;
        }
        if (keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            if (sensorValue==0f){
                audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
            }else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        print("event", Arrays.toString(sensorEvent.values));
        sensorValue = sensorEvent.values[0];
        if (sensorValue == 0f) {
            audioManager.setSpeakerphoneOn(false);
            if (player != null) {
                if (!player.isPlaying()) {
                    if (autoPlayClose) playEarpiece(false, true);
                } else {
                    playEarpiece(false, true);
                }
            } else playEarpiece(false, true);
        } else {
            audioManager.setSpeakerphoneOn(true);
            if (player != null) {
                if (player.isPlaying()) {
                    playSpeaker(false, !autoPauseFar);
                }
            } else playSpeaker(false, true);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        print("accuracyChanged to ", String.valueOf(i));
    }

    private boolean permissionGranted(){
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void editSongInfo(){
        showEditDialog(this, object -> {
            String[] values = (String[]) object;
            ID3v2 tags = getTags();
            if (tags!=null){
                tags.setTitle(values[0]);
                tags.setArtist(values[1]);
                tags.setAlbum(values[2]);
                setTags(tags);
                if (save()) {
                    Toast.makeText(this, getString(R.string.updated), Toast.LENGTH_SHORT).show();
                    b.title.setText(getString(R.string.title,values[0]));
                    b.artist.setText(getString(R.string.artist,values[1]));
                    b.album.setText(getString(R.string.album,values[2]));
                    currentMusic.setTitle(values[0]);
                    currentMusic.setArtist(values[1]);
                    currentMusic.setAlbum(values[2]);
                    editContentValues(values);
                } else Toast.makeText(this, R.string.updatingFailed, Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, R.string.failedGetInfo, Toast.LENGTH_SHORT).show();

        });
    }

    private void editContentValues(String[] values) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Audio.Media.TITLE,values[0]);
        contentValues.put(MediaStore.Audio.Media.ARTIST,values[1]);
        contentValues.put(MediaStore.Audio.Media.ALBUM,values[2]);
        getContentResolver().update(uri,contentValues,MediaStore.Audio.Media.DATA+" = ?",new String[]{currentMusic.getPath()});
    }

    private ID3v2 getTags(){
        try {
            mp3File = new Mp3File(currentMusic.getPath());
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            print("error",e.getLocalizedMessage());
            Toast.makeText(this, getString(R.string.error,e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }
        if (mp3File!=null){
            if (mp3File.hasId3v2Tag()) return mp3File.getId3v2Tag();
            if (mp3File.hasId3v1Tag()) return null;
            return new ID3v24Tag();
        }else return null;
    }

    private void setTags(ID3v2 tag){
        if (mp3File!=null) mp3File.setId3v2Tag(tag); else Toast.makeText(this, R.string.failedGetInfo, Toast.LENGTH_SHORT).show();
    }

    private boolean save(){
        String path = currentMusic.getPath(),tmp = path+"tmp";
        try {
            mp3File.save(tmp);
            File f = new File(tmp);
            boolean deleted = new File(path).delete();
            if (deleted) return f.renameTo(new File(path));
        } catch (IOException | NotSupportedException e) {
            print("error",e.getLocalizedMessage());
            Toast.makeText(this, getString(R.string.error,e.getLocalizedMessage()), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void startWork() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (player != null) {
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("a", getDuration(player.getCurrentPosition()) + " / " + getDuration(player.getDuration()));
                    message.setData(bundle);
                    if (handler != null) handler.sendMessage(message);
                }
            }
        }, 0, 1000);
        if (wakeLock.isHeld()) wakeLock.release();
        wakeLock.acquire(player.getDuration() * 2L);
        player.setOnCompletionListener(mediaPlayer -> {
            switch (mode) {
                case inOrder:
                    //playNext
                    playNext();
                    break;
                case shuffle:
                    //shuffle
                    shuffle();
                    break;
            }
        });
        b.restart.setOnClickListener(c -> {
            if (sensorValue==0f) playEarpiece(true,true); else playSpeaker(true, true);
        });
        b.play.setOnClickListener(view -> {
            if (player.isPlaying()) {
                player.pause();
                b.play.setImageResource(R.drawable.ic_play);
            } else {
                player.start();
                b.play.setImageResource(R.drawable.ic_pause);
            }
        });
        b.replay10.setOnClickListener(d -> {
            int p = player.getCurrentPosition() - 10000;
            player.seekTo(Math.max(p, 0));
        });
        b.next.setOnClickListener(c -> {
            switch (mode) {
                case inOrder:
                    playNext();
                    break;
                case shuffle:
                    shuffle();
                    break;
                case repeat:
                    playSpeaker(true, true);
                    break;
            }
        });
        b.prev.setOnClickListener(c -> {
            switch (mode) {
                case inOrder:
                    playPrevious();
                    break;
                case shuffle:
                    shuffle();
                    break;
                case repeat:
                    playSpeaker(true, true);
                    break;
            }
        });
        b.shuffle.setOnClickListener(c -> {
            if (mode != Mode.shuffle) preferences.edit().putString(playMode, shuffle).apply();
            else
                preferences.edit().putString(playMode, inOrder).apply();
            applyMode();
        });
        b.repeat.setOnClickListener(c -> {
            if (mode != Mode.repeat) preferences.edit().putString(playMode, repeat).apply();
            else
                preferences.edit().putString(playMode, inOrder).apply();
            applyMode();
        });
        b.title.setText(getString(R.string.title,currentMusic.getTitle()));
        b.artist.setText(getString(R.string.artist,currentMusic.getArtist()));
        b.album.setText(getString(R.string.album,currentMusic.getAlbum()));
    }

    private void applyMode() {
        switch (preferences.getString(playMode, inOrder)) {
            case shuffle:
                mode = Mode.shuffle;
                b.shuffle.setImageResource(R.drawable.ic_shuffle_on);
                b.repeat.setImageResource(R.drawable.ic_repeat_off);
                break;
            case repeat:
                mode = Mode.repeat;
                b.shuffle.setImageResource(R.drawable.ic_shuffle_off);
                b.repeat.setImageResource(R.drawable.ic_repeat_on);
                break;
            case inOrder:
                mode = Mode.inOrder;
                b.shuffle.setImageResource(R.drawable.ic_shuffle_off);
                b.repeat.setImageResource(R.drawable.ic_repeat_off);
                break;
        }
        player.setLooping(mode == Mode.repeat);
    }

    private void playEarpiece(boolean resetPosition, boolean needPlay) {
        if (player != null) {
            position = player.getCurrentPosition();
            player.reset();
            player.release();
            player = null;
        }
        file = new File(currentMusic.getPath());
        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
        try {
            player.setDataSource(this, Uri.fromFile(file));
            player.prepareAsync();
            player.setOnPreparedListener(mediaPlayer -> {
                if (needPlay) player.start();
                if (!resetPosition) player.seekTo(position);
                applyMode();
                startWork();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playSpeaker(boolean resetPosition, boolean needPlay) {
        if (player != null) {
            position = player.getCurrentPosition();
            player.reset();
            player.release();
            player = null;
        }
        file = new File(currentMusic.getPath());
        player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build());
        try {
            player.setDataSource(this, Uri.fromFile(file));
            player.prepareAsync();
            player.setOnPreparedListener(mediaPlayer -> {
                if (needPlay) player.start();
                if (!resetPosition) player.seekTo(position);
                applyMode();
                startWork();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void shuffle() {
        Random random = new Random();
        currentPosition = random.nextInt(currentFolder.size() - 1);
        currentMusic = currentFolder.get(currentPosition);
        if (sensorValue != 0f) playSpeaker(true, true);
        else playEarpiece(true, true);
    }

    void playNext() {
        if (currentPosition == currentFolder.size() - 1) currentPosition = 0;
        else currentPosition += 1;
        currentMusic = currentFolder.get(currentPosition);
        if (sensorValue != 0f) playSpeaker(true, true);
        else playEarpiece(true, true);
    }

    void playPrevious() {
        if (currentPosition == 0) currentPosition = currentFolder.size() - 1;
        else currentPosition -= 1;
        currentMusic = currentFolder.get(currentPosition);
        if (sensorValue != 0f) playSpeaker(true, true);
        else playEarpiece(true, true);
    }
}