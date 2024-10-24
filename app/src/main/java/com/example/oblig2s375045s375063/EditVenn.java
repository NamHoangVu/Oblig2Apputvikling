package com.example.oblig2s375045s375063;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class EditVenn extends AppCompatActivity {
    private VennerDataKilde dataKilde;

    private EditText navnEditText, telefonEditText, bursdagEditText;
    private TextView navnTextView, telefonTextView, bursdagTextView;
    private long vennId;

    Button endreVennKnapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_venn);

        endreVennKnapp = findViewById(R.id.endreVennKnapp);
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);

        navnTextView = findViewById(R.id.navnTextView);
        telefonTextView = findViewById(R.id.telefonTextView);
        bursdagTextView = findViewById(R.id.bursdagTextView);

        Intent intent = getIntent();
        vennId = intent.getLongExtra("vennId", -1);
        String navn = intent.getStringExtra("navn");
        String telefon = intent.getStringExtra("telefon");
        String bursdag = intent.getStringExtra("bursdag");

        if (navn != null) {
            navnTextView.setText(navn);
        }
        if (telefon != null) {
            telefonTextView.setText(telefon);
        }
        if (bursdag != null) {
            bursdagTextView.setText(bursdag);

        }

        dataKilde = new VennerDataKilde(this);
        dataKilde.open();


        endreVennKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hent oppdaterte verdier fra EditText-feltene
                String nyttNavn = navnEditText.getText().toString();
                String nyTelefon = telefonEditText.getText().toString();
                String nyBursdag = bursdagEditText.getText().toString();
                if (!nyttNavn.isEmpty() && !nyTelefon.isEmpty() && !nyBursdag.isEmpty()) {
                    dataKilde.endreVenn(vennId, nyttNavn, nyTelefon, nyBursdag);

                    Toast.makeText(EditVenn.this, "Vennens informasjon er oppdatert!", Toast.LENGTH_SHORT).show();

                    // Start MainActivity på nytt for å vise den oppdaterte listen
                    Intent intent = new Intent(EditVenn.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Fjern alle aktiviteter på stacken og gå tilbake til MainActivity
                    startActivity(intent);

                    // Avslutt EditVenn-aktiviteten slik at brukeren ikke kan gå tilbake hit
                    finish();
                }
            }
        });

        bursdagEditText.setOnClickListener(v -> {
            // Få dagens dato som standard
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            // Åpne DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(EditVenn.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Oppdater EditText med den valgte datoen
                        bursdagEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);
            datePickerDialog.show();
        });

        Button slettKnapp = findViewById(R.id.slettKnapp); // Sørg for at knappen eksisterer i layout
        slettKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slett(v);
            }

        });
    }

        // Slett venn-funksjon
        public void slett (View v){
            if (vennId != -1) {
                dataKilde.slettVenn(vennId);

                Toast.makeText(EditVenn.this, "Venn slettet!", Toast.LENGTH_SHORT).show();

                // Start MainActivity på nytt for å vise den oppdaterte listen
                Intent intent = new Intent(EditVenn.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Fjern alle aktiviteter på stacken og gå tilbake til MainActivity
                startActivity(intent);

                // Avslutt EditVenn-aktiviteten slik at brukeren ikke kan gå tilbake hit
                finish();
            }
        }

        @Override
        public void onDestroy () {
            super.onDestroy();
            dataKilde.close();
        }

}
