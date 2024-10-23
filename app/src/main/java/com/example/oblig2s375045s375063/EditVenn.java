package com.example.oblig2s375045s375063;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class EditVenn extends AppCompatActivity {
    private VennerDataKilde dataKilde;

    private EditText vennIDEditText, navnEditText, telefonEditText, bursdagEditText;

    Button endreVennKnapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_venn);

        endreVennKnapp = findViewById(R.id.endreVennKnapp);
        vennIDEditText = findViewById(R.id.vennIDEditText);
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);

        dataKilde = new VennerDataKilde(this);
        dataKilde.open();


        endreVennKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hent oppdaterte verdier fra EditText-feltene
                String vennID = vennIDEditText.getText().toString();
                String navn = navnEditText.getText().toString();
                String telefon = telefonEditText.getText().toString();
                String bursdag = bursdagEditText.getText().toString();
                if (!vennID.isEmpty() && !navn.isEmpty() && !telefon.isEmpty() && !bursdag.isEmpty()) {
                    dataKilde.endreVenn(vennID, navn, telefon, bursdag);
                    vennIDEditText.setText("");
                    navnEditText.setText("");
                    telefonEditText.setText("");
                    bursdagEditText.setText("");
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
    }
}
