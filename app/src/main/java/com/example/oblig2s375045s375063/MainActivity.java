package com.example.oblig2s375045s375063;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VennAdapter.OnVennClickListener {
    private SmsHandler smsHandler;

    private RecyclerView recyclerView;
    private VennAdapter vennAdapter;
    private List<Venn> vennList;
    private VennerDataKilde dataKilde;
    private EditText navnEditText, telefonEditText, bursdagEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Åpne databasen
        dataKilde = new VennerDataKilde(this);
        dataKilde.open();

        Button openPreferencesButton = findViewById(R.id.open_preferences_button);

        // Sett OnClickListener for knappen
        openPreferencesButton.setOnClickListener(this::openPreferences);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                    systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vennList = dataKilde.finnAlleVenner();

        vennAdapter = new VennAdapter(vennList, new VennAdapter.OnVennClickListener() {
            @Override
            public void onItemClick(Venn venn) {
                Intent intent = new Intent(MainActivity.this, EditVenn.class);
                intent.putExtra("vennId", venn.getId());
                intent.putExtra("navn", venn.getNavn());
                intent.putExtra("telefon", venn.getTelefon());
                intent.putExtra("bursdag", venn.getBursdag());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(vennAdapter);

        // Finn EditTexts og TextView
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);

        // Set up DatePickerDialog for bursdagEditText
        bursdagEditText.setOnClickListener(v -> {
            // Få dagens dato som standard
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // Åpne DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Oppdater EditText med den valgte datoen
                        bursdagEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);
            datePickerDialog.show();
        });

        BroadcastReceiver myBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter;
        filter = new IntentFilter("com.example.service.MITTSIGNAL");
        filter.addAction("com.example.service.MITTSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter, Context.RECEIVER_EXPORTED);


        smsHandler = new SmsHandler(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Be om tillatelse
            smsHandler.sendSms("", "");  // Dette vil initiere tillatelsessjekk
        } else {
            // Tillatelse allerede gitt, send en test SMS om nødvendig
            Toast.makeText(this, "Tillatelse allerede gitt.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPreferences(View v) {
        // Skjul RecyclerView, knapper, og TextView
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.navnEditText).setVisibility(View.GONE);
        findViewById(R.id.telefonEditText).setVisibility(View.GONE);
        findViewById(R.id.bursdagEditText).setVisibility(View.GONE);
        findViewById(R.id.leggtil).setVisibility(View.GONE);
        findViewById(R.id.open_preferences_button).setVisibility(View.GONE); // Skjul preferanser knappen

        // Åpne preferansefragmentet
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_container, new PreferanseFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        // Sjekk om det er fragmenter i tilbake-stakken
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Fjern det øverste fragmentet
            getSupportFragmentManager().popBackStack();

            // Vis RecyclerView, knapper, og TextView igjen
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.navnEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.telefonEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.bursdagEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.leggtil).setVisibility(View.VISIBLE);
            findViewById(R.id.open_preferences_button).setVisibility(View.VISIBLE); // Vis preferanser knappen
        } else {
            // Hvis ingen fragmenter er i stakken, kjør standard atferd
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Send SMS hvis tillatelse er gitt
        smsHandler.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onItemClick(Venn venn) {
    }

    // Legg til venn-funksjon
    public void leggtil(View v) {
        String navn = navnEditText.getText().toString();
        String telefon = telefonEditText.getText().toString();
        String bursdag = bursdagEditText.getText().toString();
        if (!navn.isEmpty() && !telefon.isEmpty() && !bursdag.isEmpty()) {
            dataKilde.leggTilVenn(navn, telefon, bursdag);

            // Oppdater vennelisten
            vennList.clear();
            vennList.addAll(dataKilde.finnAlleVenner());
            vennAdapter.notifyDataSetChanged();

            navnEditText.setText("");
            telefonEditText.setText("");
            bursdagEditText.setText("");
        }
    }

    @Override
    protected void onResume() {
        dataKilde.open();
        super.onResume();

        vennList.clear();
        vennList.addAll(dataKilde.finnAlleVenner());
        vennAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        dataKilde.close();
        super.onPause();
    }
}

