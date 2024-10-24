package com.example.oblig2s375045s375063;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MinSendService extends Service {

    private VennerDataKilde dataKilde;
    private SmsHandler smsHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialiser databasen
        dataKilde = new VennerDataKilde(this);
        dataKilde.open();

        // Initialiser SMS-handleren
        smsHandler = new SmsHandler(this);

        Log.d("MinSendService", "Service opprettet og databasen er åpnet.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MinSendService", "Service startet.");

        // Hent dagens dato
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String dagensDato = dateFormat.format(calendar.getTime());

        Log.d("MinSendService", "Dagens dato: " + dagensDato);

        // Sjekk hvem som har bursdag i dag
        List<Venn> vennerMedBursdag = dataKilde.hentVennerMedBursdag(dagensDato);

        Log.d("MinSendService", "Antall venner med bursdag i dag: " + vennerMedBursdag.size());

        // Hent standardmeldingen fra SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String standardMelding = sharedPreferences.getString("sms_message", "Hei, dette er en automatisk SMS.");
        Log.d("MinSendService", "Standardmelding hentet fra preferanser: " + standardMelding);

        // Send gratulasjons-SMS til dem som har bursdag
        for (Venn venn : vennerMedBursdag) {
            String telefonnummer = venn.getTelefon();
            String navn = venn.getNavn();

            // Sett inn vennens navn i standardmeldingen
            String melding = standardMelding.replace("{navn}", navn);

            // Logg informasjon før SMS sendes
            Log.d("MinSendService", "Sender melding til: " + navn + " på telefonnummer: " + telefonnummer);

            smsHandler.sendSms(telefonnummer, melding);

            // Bekreft at melding er sendt
            Log.d("MinSendService", "Melding sendt til: " + navn);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataKilde.close();
        Log.d("MinSendService", "Service stoppet og databasen lukket.");
    }
}
