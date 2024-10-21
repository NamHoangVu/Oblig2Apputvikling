package com.example.oblig2s375045s375063;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

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
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Minservice", "I min service");

        // Hent dagens dato
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
        String dagensDato = dateFormat.format(calendar.getTime());

        // Sjekk hvem som har bursdag i dag
        List<Venn> vennerMedBursdag = dataKilde.hentVennerMedBursdag(dagensDato);

        // Send gratulasjons-SMS til dem som har bursdag
        for (Venn venn : vennerMedBursdag) {
            String telefonnummer = venn.getTelefon();
            String navn = venn.getNavn();
            String melding = "Gratulerer med dagen, " + navn + "!";
            smsHandler.sendSms(telefonnummer, melding);
            Log.d("Minservice", "Sendt melding til " + navn);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataKilde.close();
        Log.d("Minservice", "Service fjernet");
    }
}
