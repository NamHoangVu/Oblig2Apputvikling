package com.example.oblig2s375045s375063;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class MinPeriodisk extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MinPeriodisk", "MinPeriodisk har startet.");

        // Hent SharedPreferences med standardnavn
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.oblig2s375045s375063_preferences", MODE_PRIVATE);

        // Hent tidspunktet fra SharedPreferences
        String time = sharedPreferences.getString("sms_time", "08:00");

        // Split time into hour and minute
        String[] timeParts = time.split(":");
        if (timeParts.length == 2) {
            try {
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);

                // Sett kalender til neste gang alarmen skal gå av
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                // Sjekk om tiden allerede har passert for i dag, i så fall, sett alarmen for neste dag
                if (cal.getTimeInMillis() < System.currentTimeMillis()) {
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }

                Intent i = new Intent(this, MinSendService.class);
                PendingIntent pintent = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Fjern eksisterende alarm hvis det finnes, før vi setter opp en ny
                if (alarm != null) {
                    alarm.cancel(pintent);
                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
                    Log.d("MinPeriodisk", "Alarm opprettet: " + cal.getTime());

                    // Start MinSendService når alarmen går av
                    Log.d("MinPeriodisk", "MinSendService vil bli startet av alarmen.");
                } else {
                    Log.e("MinPeriodisk", "Kunne ikke hente AlarmManager.");
                }

            } catch (NumberFormatException e) {
                Log.e("MinPeriodisk", "Feil ved parsing av tid: " + e.getMessage());
            }
        } else {
            Log.e("MinPeriodisk", "Ugyldig tidformat fra SharedPreferences: " + time);
        }

        return START_STICKY; // Sørg for at tjenesten blir gjenopprettet hvis systemet terminerer den
    }
}
