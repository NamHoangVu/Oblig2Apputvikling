package com.example.oblig2s375045s375063;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        dataKilde = new VennerDataKilde(this);
        dataKilde.open();
        navnEditText = findViewById(R.id.navnEditText);
        telefonEditText = findViewById(R.id.telefonEditText);
        bursdagEditText = findViewById(R.id.bursdagEditText);
        vennEditText = findViewById(R.id.vennEditText);
        // men brukes senere tekstView=findViewById(R.id.visview);
    }
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
    public void slett(View v) {
        long vennId =
                Long.parseLong(String.valueOf(vennEditText.getText()));
        dataKilde.slettVenn(vennId);
    }
    public void visAlle(View v) {
        String tekst = "";
        List<Venn> oppgaver = dataKilde.finnAlleVenner();
        for (Venn venn : venner) {
            tekst = tekst + " " + venn.getNavn();
        }
        tekstView.setText(tekst);
    }


}