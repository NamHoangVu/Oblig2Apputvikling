package com.example.oblig2s375045s375063;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "venner.db";
    private static final int DATABASE_VERSJON = 1;

    public static final String TABELL_NAVN = "venner";
    public static final String KOLONNE_ID = "id";
    public static final String KOLONNE_NAVN = "navn";
    public static final String KOLONNE_TELEFON = "telefon";
    public static final String KOLONNE_BURSDAG = "bursdag";

    public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSJON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String OPPRETT_TABELL = "CREATE TABLE " + TABELL_NAVN + "("
                + KOLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KOLONNE_NAVN + " TEXT,"
                + KOLONNE_TELEFON + " TEXT,"
                + KOLONNE_BURSDAG + " TEXT)";
        db.execSQL(OPPRETT_TABELL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int gammelVersjon, int nyVersjon) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELL_NAVN);
        onCreate(db);
    }

    // Metoder for å håndtere data i databasen

    public void leggTilVenn(String navn, String telefon, String bursdag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues verdier = new ContentValues();
        verdier.put(KOLONNE_NAVN, navn);
        verdier.put(KOLONNE_TELEFON, telefon);
        verdier.put(KOLONNE_BURSDAG, bursdag);

        db.insert(TABELL_NAVN, null, verdier);
        db.close();
    }

    public Cursor hentAlleVenner() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABELL_NAVN, null);
    }

    public void oppdaterVenn(int id, String navn, String telefon, String bursdag) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues verdier = new ContentValues();
        verdier.put(KOLONNE_NAVN, navn);
        verdier.put(KOLONNE_TELEFON, telefon);
        verdier.put(KOLONNE_BURSDAG, bursdag);

        db.update(TABELL_NAVN, verdier, KOLONNE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void slettVenn(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELL_NAVN, KOLONNE_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}

