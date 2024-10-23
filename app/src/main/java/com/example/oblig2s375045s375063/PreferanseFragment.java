package com.example.oblig2s375045s375063;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.EditTextPreference;
import androidx.preference.SwitchPreferenceCompat;
import android.app.TimePickerDialog;
import android.util.Log;
import java.util.Calendar;

public class PreferanseFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Hent SharedPreferences
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();

        // Håndter tidspicker for SMS-tid
        Preference timePreference = findPreference("sms_time");
        if (timePreference != null) {
            String defaultTime = "08:00";  // Standardverdi
            timePreference.setSummary(sharedPreferences.getString("sms_time", defaultTime));
        }

        // Håndter endring av tid
        timePreference.setOnPreferenceClickListener(preference -> {
            showTimePickerDialog(sharedPreferences); // Passer inn sharedPreferences
            return true;
        });

        // Håndter SMS-tjeneste switch
        SwitchPreferenceCompat smsServiceSwitch = findPreference("sms_service_enabled");
        if (smsServiceSwitch != null) {
            smsServiceSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;

                // Lagre tilstanden til tjenesten
                sharedPreferences.edit().putBoolean("sms_service_enabled", isChecked).apply();

                // Send intent for å starte/stopp MinPeriodisk
                Intent intent = new Intent(getContext(), MinPeriodisk.class);
                if (isChecked) {
                    getContext().startService(intent);
                } else {
                    getContext().stopService(intent);
                }

                return true;  // Returner true for å lagre endringen
            });
        }
    }

    private void showTimePickerDialog(SharedPreferences sharedPreferences) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, selectedHour, selectedMinute) -> {
                    // Lagre valgt tid i formatet HH:mm
                    String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);

                    // Hent preferansen for tid og oppdater oppsummeringen
                    Preference timePreference = findPreference("sms_time");
                    if (timePreference != null) {
                        timePreference.setSummary(selectedTime);

                        // Lagre den valgte tiden i SharedPreferences
                        sharedPreferences.edit()
                                .putString("sms_time", selectedTime)
                                .apply();

                        // Logg for å bekrefte at tiden blir lagret
                        Log.d("PreferanseFragment", "Ny tid lagret i SharedPreferences: " + selectedTime);

                        // Start MinPeriodisk på nytt for å oppdatere alarmen
                        Intent intent = new Intent(getContext(), MinPeriodisk.class);
                        getContext().startService(intent);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}
