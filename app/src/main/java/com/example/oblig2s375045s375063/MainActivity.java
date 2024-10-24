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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    private VennerDataKilde dataKilde;
    private EditText slettVennEditText, navnEditText, telefonEditText, bursdagEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button openPreferencesButton = findViewById(R.id.open_preferences_button);
        Button endreVennKnapp = findViewById(R.id.endreVennKnapp);

        // Onclick for endre venn knapp
        endreVennKnapp.setOnClickListener(v -> nyAktivitet(EditVenn.class));

        // Sett OnClickListener for preferanseknappen
        openPreferencesButton.setOnClickListener(this::openPreferences);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialiser adapter og liste
        List<Venn> vennListe = new ArrayList<>();
        vennAdapter = new VennAdapter(vennListe, this);
        recyclerView.setAdapter(vennAdapter);

        // Åpne databasen
        dataKilde = new VennerDataKilde(this);
        dataKilde.open();

        // Finn EditTexts
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);
        slettVennEditText = findViewById(R.id.slettVennEditText);

        // Set up DatePickerDialog for bursdagEditText
        bursdagEditText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        bursdagEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);
            datePickerDialog.show();
        });

        visAlle();

        BroadcastReceiver myBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.service.MITTSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter, Context.RECEIVER_EXPORTED);

        smsHandler = new SmsHandler(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            smsHandler.sendSms("", "");  // Dette vil initiere tillatelsessjekk
        } else {
            Toast.makeText(this, "Tillatelse allerede gitt.", Toast.LENGTH_SHORT).show();
        }
    }

    // Metode for å starte en ny aktivitet
    private void nyAktivitet(Class<?> k) {
        Intent i = new Intent(this, k);
        startActivity(i); // Starter aktiviteten
    }

    private void openPreferences(View v) {
        // Skjul RecyclerView og EditTexts
        recyclerView.setVisibility(View.GONE);
        navnEditText.setVisibility(View.GONE);
        telefonEditText.setVisibility(View.GONE);
        bursdagEditText.setVisibility(View.GONE);
        findViewById(R.id.leggtil).setVisibility(View.GONE);
        findViewById(R.id.slett).setVisibility(View.GONE);
        findViewById(R.id.open_preferences_button).setVisibility(View.GONE);
        findViewById(R.id.slettVennEditText).setVisibility(View.GONE);
        findViewById(R.id.endreVennKnapp).setVisibility(View.GONE);

        // Åpne preferansefragmentet
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_container, new PreferanseFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // Vis RecyclerView og EditTexts igjen
            recyclerView.setVisibility(View.VISIBLE);
            navnEditText.setVisibility(View.VISIBLE);
            telefonEditText.setVisibility(View.VISIBLE);
            bursdagEditText.setVisibility(View.VISIBLE);
            findViewById(R.id.leggtil).setVisibility(View.VISIBLE);
            findViewById(R.id.slett).setVisibility(View.VISIBLE);
            findViewById(R.id.open_preferences_button).setVisibility(View.VISIBLE);
            findViewById(R.id.slettVennEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.endreVennKnapp).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsHandler.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void onItemClick(Venn venn) {
        // Håndterer når et element i RecyclerView blir klikket
        // Kan implementeres etter behov
    }

    // Legg til venn-funksjon
    public void leggtil(View v) {
        String navn = navnEditText.getText().toString();
        String telefon = telefonEditText.getText().toString();
        String bursdag = bursdagEditText.getText().toString();
        if (!navn.isEmpty() && !telefon.isEmpty() && !bursdag.isEmpty()) {
            dataKilde.leggTilVenn(navn, telefon, bursdag);
            navnEditText.setText("");
            telefonEditText.setText("");
            bursdagEditText.setText("");
            visAlle();
        }
    }

    @Override
    protected void onResume() {
        dataKilde.open();
        super.onResume();
        visAlle();
    }

    @Override
    protected void onPause() {
        dataKilde.close();
        super.onPause();
    }

    // Slett venn-funksjon
    public void slett(View v) {
        long vennId = Long.parseLong(slettVennEditText.getText().toString());
        dataKilde.slettVenn(vennId);
        slettVennEditText.setText("");
        visAlle();
    }

    // Vis alle venner-funksjon
    public void visAlle() {
        List<Venn> venner = dataKilde.finnAlleVenner();
        vennAdapter.updateVennListe(venner); // Oppdaterer adapteren med ny liste
    }
}
