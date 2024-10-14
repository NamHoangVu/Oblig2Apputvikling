package com.example.oblig2s375045s375063;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VennAdapter.OnVennClickListener {

    private RecyclerView recyclerView;
    private VennAdapter vennAdapter;
    private VennAdapter.OnVennClickListener listen;

    private List<Venn> venner;
    private VennerDataKilde dataKilde;
    private EditText vennEditText, navnEditText, telefonEditText, bursdagEditText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right,
                    systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Venn> VennList = new ArrayList<>();
        vennAdapter = new VennAdapter(VennList, this);
        recyclerView.setAdapter(vennAdapter);

        // Åpne databasen
        dataKilde = new VennerDataKilde(this);
        dataKilde.open();

        // Finn EditTexts og TextView
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);
        // tekstView brukes senere
        textView = findViewById(R.id.visview);

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
        visAlle();
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
            Venn venn = dataKilde.leggTilVenn(navn, telefon, bursdag);
            navnEditText.setText("");
            telefonEditText.setText("");
            bursdagEditText.setText("");
        }
    }

    @Override
    protected void onResume() {
        dataKilde.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataKilde.close();
        super.onPause();
    }

    // Slett venn-funksjon
    public void slett(View v) {
        long vennId = Long.parseLong(String.valueOf(vennEditText.getText()));
        dataKilde.slettVenn(vennId);
    }

    // Vis alle venner-funksjon
    public void visAlle() {
        String tekst = "";
        List<Venn> venner = dataKilde.finnAlleVenner();
        for (Venn venn : venner) {
            tekst += " " + venn.getId() + ": " + venn.getNavn() + ", " + venn.getTelefon() + ", " + venn.getBursdag() + "\n";
        }
        textView.setText(tekst);
    }
}
