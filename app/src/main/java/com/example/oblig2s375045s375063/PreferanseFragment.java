package com.example.oblig2s375045s375063;

import android.graphics.Color;
import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.Preference;
import androidx.preference.EditTextPreference;
import androidx.preference.SwitchPreferenceCompat;
import android.app.TimePickerDialog;
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
        timePreference.setOnPreferenceClickListener(preference -> {
            showTimePickerDialog();
            return true;
        });
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                (view, selectedHour, selectedMinute) -> {
                    // Lagre valgt tid
                    String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    Preference timePreference = findPreference("sms_time");
                    if (timePreference != null) {
                        timePreference.setSummary(selectedTime);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}

