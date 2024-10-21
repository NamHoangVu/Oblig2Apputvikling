package com.example.oblig2s375045s375063;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MinBroadcastReceiver extends BroadcastReceiver {
    public MinBroadcastReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnabled = intent.getBooleanExtra("sms_service_enabled", false);
        if (isEnabled) {
            Intent i = new Intent(context, MinSendService.class);
            context.startService(i);
        }
    }
}
