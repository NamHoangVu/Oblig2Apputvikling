package com.example.oblig2s375045s375063;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MinBroadcastReceiver extends BroadcastReceiver {
    public MinBroadcastReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isEnabled = intent.getBooleanExtra("sms_service_enabled", false);
        Log.d("Receive", "Signal Mottatt!");

        Intent serviceIntent = new Intent(context, MinPeriodisk.class);

        if (isEnabled) {
            context.startService(serviceIntent);
        } else {
            context.stopService(serviceIntent);
            // Du kan også sende en melding til Logcat for å bekrefte stoppet
            Log.d("Receive", "Tjenesten stoppet.");

            Intent alarmIntent = new Intent(context, MinBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
            Log.d("Receive", "Alarmen kansellert.");
        }
    }
}
