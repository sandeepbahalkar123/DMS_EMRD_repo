package com.scorg.dms.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.scorg.dms.notification.MQTTServiceAlarmTask;

/**
 * Created by ganeshshirole on 27/6/17.
 */

public class StartUpBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            new MQTTServiceAlarmTask(context).run();
        }
    }
}