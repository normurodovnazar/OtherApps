package com.normurodov_nazar.otherapps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {
    NotificationManager manager;
    private final IBinder mBinder = new CustomBinder();
    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void showNotification(){
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(Icon.createWithResource(this,R.drawable.ic_music))
                .setContentText("setContentText")
                .setSubText("setSubText")
                .build();
        manager.notify(10,notification);
    }

    public class CustomBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }
}