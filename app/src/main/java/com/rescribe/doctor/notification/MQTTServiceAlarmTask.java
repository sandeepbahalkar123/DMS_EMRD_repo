package com.rescribe.doctor.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.rescribe.doctor.services.MQTTService;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class MQTTServiceAlarmTask implements Runnable {

    private static final int MQTT_ALARM_TASK = 87877;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    private final Calendar calendar;

    public MQTTServiceAlarmTask(Context context) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        this.calendar = Calendar.getInstance();
    }

    @Override
    public void run() {
        setAlarm();
    }

    private void setAlarm() {
        // start mqtt Service
        // use this to start and trigger a service
        Intent serviceIntent = new Intent(context, MQTTService.class);
        // potentially add data to the serviceIntent
        PendingIntent pendingIntent = PendingIntent.getService(context, MQTT_ALARM_TASK, serviceIntent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public static void cancelAlarm(Context context) {
        Intent serviceIntent = new Intent(context, MQTTService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, MQTT_ALARM_TASK, serviceIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
