package com.example.oblig2s375045s375063;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SmsHandler {

    private static final int PERMISSION_REQUEST_SEND_SMS = 1;
    private Context context;

    public SmsHandler(Context context) {
        this.context = context;
    }

    // Sjekk om tillatelse er gitt, og send SMS
    public void sendSms(String phoneNumber, String message) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Be om tillatelse
            ActivityCompat.requestPermissions((MainActivity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_SEND_SMS);
        } else {
            // Tillatelse gitt, send SMS
            sendSmsMessage(phoneNumber, message);
        }
    }

    // Håndter tillatelsesresultatet
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Tillatelse gitt. SMS kan sendes.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "SMS-tillatelse avslått.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Faktisk SMS-sending
    private void sendSmsMessage(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(context, "SMS sendt!", Toast.LENGTH_SHORT).show();
    }
}

