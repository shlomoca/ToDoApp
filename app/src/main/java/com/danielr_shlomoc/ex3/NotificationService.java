package com.danielr_shlomoc.ex3;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;


public class NotificationService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
