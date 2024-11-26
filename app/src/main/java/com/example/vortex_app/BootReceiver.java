package com.example.vortex_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Reschedule the alarm
        NotificationsActivity.scheduleStatusCheckAlarm(context);
    }
}
