package com.example.vortex_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StatusCheckReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Perform Firestore checks and notification logic
        StatusCheckTask.performStatusCheck(context);
    }
}