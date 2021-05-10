package com.danielr_shlomoc.ex3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmClockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int taskID = intent.getIntExtra("taskID", -1);
        context.startService(new Intent(context, NotificationService.class));
        NotificationHandler handler = new NotificationHandler(context);
        handler.ShowNotification(taskID);
    }
}
