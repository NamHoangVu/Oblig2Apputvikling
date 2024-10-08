package com.example.oblig2s375045s375063;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LeggTilVennActivity extends AppCompatActivity {

    private EditText tekstNavn, tekstTelefon, tekstBursdag;
    private Button knappLagre;
    private DatabaseHjelper dbHjelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legg_til_venn);

        tekstNavn = findViewById(R.id.tekstNavn);
        tekstTelefon = findViewById(R.id.tekstTelefon);
        tekstBursdag = findViewById(R.id.tekstBursdag);
        knappLagre = findViewById(R.id.knappLagre);

        dbHjelper = new DatabaseHjelper(this);

        knappLagre.setOnClickListener(v -> {
            String navn = tekstNavn.getText().toString();
            String telefon = tekstTelefon.getText().toString();
            String bursdag = tekstBursdag.getText().toString();

            dbHjelper.leggTilVenn(navn, telefon, bursdag);
            finish(); // Returnerer til forrige aktivitet
        });
    }
}

