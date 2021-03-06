package com.danielr_shlomoc.ex3;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHandler {

    private static final String CHANNEL_ID = "channel_main";
    private static final CharSequence CHANNEL_NAME = "Main Channel";
    private final NotificationManager notificationManager;
    private Context context;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    private DataBase DB;


    //    protected void onCreate(Bundle savedInstanceState) {
    public NotificationHandler(Context context) {
        this.context = context;
        // Get reference Notification Manager system Service
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmIntent = new Intent(context, AlarmClockReceiver.class);
        DB = new DataBase(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //show user a notification of a task based on its ID
    public void ShowNotification(int id) {
        Task task = DB.getTask(id);
        long time = System.currentTimeMillis();

        if (task != null) {
            boolean overMinuteDifference = Math.abs(time - task.getDateTime()) > 61 * 1000;
            if (!overMinuteDifference) {
                Log.i("mylog", "is it over minute? " + overMinuteDifference + " current time = " + time + " task time = " + task.getDateTime());
                Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.todo_icon_round)
                        .setContentTitle(task.getTitle())
                        .setContentText(task.getDescription())
                        .setPriority(NotificationCompat.PRIORITY_HIGH).build();
                notificationManager.notify(task.getId(), notification);
            }
        }


    }

    public void cancelAlarm(int taskID) {
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, taskID, alarmIntent, PendingIntent.FLAG_NO_CREATE);
        if (alarmPendingIntent != null) {
            alarmManager.cancel(alarmPendingIntent);
            alarmPendingIntent.cancel();
            Log.d("mylog", ">>> Alarm Canceled!");
        } else
            Log.d("mylog", "Alarm does'nt exist id: " + taskID);
    }

    // Create an alarm for a task
    public void createOneTimeAlarm(Task task) {

        // Create Intent to call the BroadcastReceiver
        Intent alarmIntent = new Intent(context, AlarmClockReceiver.class);
        int taskID = task.getId();
        alarmIntent.putExtra("taskID", taskID);

        // Create PendingIntent to start the BroadcastReceiver when alarm is triggered.
        // Params:
        // 1. Context - activity context - this
        // 2. int - the id of the alarm - ALARM_ID
        // 3. Intent - intent - alarmIntent
        // 4. int - flag - PendingIntent.FLAG_UPDATE_CURRENT
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, taskID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Set the OneTime Alarm
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.getDateTime(), alarmPendingIntent);
            Log.d("mylog", String.format(">>> Set Alarm Id: %d. to START at %s time: %s", taskID, task.getDate(), task.getTime()));
        }
    }
}
