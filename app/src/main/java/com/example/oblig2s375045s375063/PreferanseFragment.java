package com.example.oblig2s375045s375063;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.EditTextPreference;
import androidx.preference.SwitchPreferenceCompat;
import android.app.TimePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import java.util.Calendar;

public class PreferanseFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Håndter tidspicker for SMS-tid
        Preference timePreference = findPreference("sms_time");
        if (timePreference != null) {
            String defaultTime = "08:00";  // Standardverdi
            timePreference.setSummary(getPreferenceManager().getSharedPreferences().getString("sms_time", defaultTime));
        }

        // Håndter standard SMS-melding
        EditTextPreference messagePreference = findPreference("sms_message");
        if (messagePreference != null) {
            messagePreference.setSummary(messagePreference.getText());
        }

        // Håndter endring av tid
        timePreference.setOnPreferenceClickListener(preference -> {
            showTimePickerDialog();
            return true;
        });

        // Håndter SMS-tjeneste switch
        SwitchPreferenceCompat smsServiceSwitch = findPreference("sms_service_enabled");
        if (smsServiceSwitch != null) {
            smsServiceSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isChecked = (Boolean) newValue;

                // Lag en intent for å sende til BroadcastReceiver
                Intent intent = new Intent("com.example.service.MITTSIGNAL");
                intent.putExtra("sms_service_enabled", isChecked);

                // Send broadcast
                getContext().sendBroadcast(intent);

                return true;  // Returner true for å lagre endringen
            });
        }
    }
    private void showTimePickerDialog() {
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
                        getPreferenceManager().getSharedPreferences().edit()
                                .putString("sms_time", selectedTime)
                                .apply();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}

