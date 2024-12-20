package com.example.oblig2s375045s375063;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VennAdapter.OnVennClickListener {
    private SmsHandler smsHandler;

    private RecyclerView recyclerView;
    private VennAdapter vennAdapter;
    private List<Venn> vennList;
    private VennerDataKilde dataKilde;
    private EditText navnEditText, telefonEditText, bursdagEditText;

    String CHANNEL_ID = "MinKanal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        dataKilde = new VennerDataKilde(this);
        dataKilde.open();

        Button openPreferencesButton = findViewById(R.id.open_preferences_button);

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

        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);

        // Setter opp DatePickerDialog for bursdagEditText
        bursdagEditText.setOnClickListener(v -> {
            // Få dagens dato som standard
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

        BroadcastReceiver myBroadcastReceiver = new MinBroadcastReceiver();
        IntentFilter filter;
        filter = new IntentFilter("com.example.service.MITTSIGNAL");
        filter.addAction("com.example.service.MITTSIGNAL");
        this.registerReceiver(myBroadcastReceiver, filter, Context.RECEIVER_EXPORTED);


        smsHandler = new SmsHandler(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Be om tillatelse
            smsHandler.sendSms("", "");
        }
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new
                NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager =
                getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void openPreferences(View v) {
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.navnEditText).setVisibility(View.GONE);
        findViewById(R.id.telefonEditText).setVisibility(View.GONE);
        findViewById(R.id.bursdagEditText).setVisibility(View.GONE);
        findViewById(R.id.leggtil).setVisibility(View.GONE);
        findViewById(R.id.open_preferences_button).setVisibility(View.GONE); // Skjul preferanser knappen

        // Gjør preferansefragmentet synlig
        findViewById(R.id.settings_container).setVisibility(View.VISIBLE);

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
            getSupportFragmentManager().popBackStack();

            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.navnEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.telefonEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.bursdagEditText).setVisibility(View.VISIBLE);
            findViewById(R.id.leggtil).setVisibility(View.VISIBLE);
            findViewById(R.id.open_preferences_button).setVisibility(View.VISIBLE);
        } else {
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
        String navn = navnEditText.getText().toString().trim();
        String telefon = telefonEditText.getText().toString().trim();
        String bursdag = bursdagEditText.getText().toString().trim();

        // Input validering for navn
        if (navn.isEmpty() || !navn.matches("[a-zA-ZæøåÆØÅ ]+")) {
            navnEditText.setError("Vennligst skriv inn et gyldig navn uten tall.");
            return;
        } else {
            navnEditText.setError(null);
        }

        // Input validering for telefon
        if (telefon.isEmpty() || !telefon.matches("\\d{8}")) {
            telefonEditText.setError("Vennligst skriv inn et gyldig telefonnummer (8 sifre).");
            return;
        } else {
            telefonEditText.setError(null);
        }

        // Input validering for bursdag
        if (bursdag.isEmpty()) {
            bursdagEditText.setError("Vennligst velg en dato");
            return;
        } else {
            bursdagEditText.setError(null);
        }

        // Hvis alle felt er gyldige, legg til venn
        dataKilde.leggTilVenn(navn, telefon, bursdag);

        // Oppdater vennelisten
        vennList.clear();
        vennList.addAll(dataKilde.finnAlleVenner());
        vennAdapter.notifyDataSetChanged();

        // Tøm inputfeltene etter vellykket lagring
        navnEditText.setText("");
        telefonEditText.setText("");
        bursdagEditText.setText("");
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

