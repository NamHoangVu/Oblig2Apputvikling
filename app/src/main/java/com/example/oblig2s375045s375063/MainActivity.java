package com.example.oblig2s375045s375063;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VennAdapter adapter;
    private DatabaseHjelper dbHjelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewVenner);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHjelper = new DatabaseHjelper(this);

        lastInnVenner();
    }

    private void lastInnVenner() {
        Cursor cursor = dbHjelper.hentAlleVenner();
        List<Venn> venner = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                venner.add(new Venn(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_NAVN)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_TELEFON)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_BURSDAG))
                ));
            } while (cursor.moveToNext());
        }

        adapter = new VennAdapter(venner);
        recyclerView.setAdapter(adapter);
    }
}