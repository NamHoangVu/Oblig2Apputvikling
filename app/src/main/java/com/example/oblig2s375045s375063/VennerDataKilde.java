package com.example.oblig2s375045s375063;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class VennerDataKilde {
    private SQLiteDatabase database;
    private DatabaseHjelper dbHjelper;

    public VennerDataKilde(Context context) {
        dbHjelper = new DatabaseHjelper(context);
    }
    public void open() throws SQLException {
        database = dbHjelper.getWritableDatabase();
    }
    public void close() {
        dbHjelper.close();
    }

    public Venn leggTilVenn(String navn, String telefon, String bursdag) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHjelper.KOLONNE_NAVN, navn);
        values.put(DatabaseHjelper.KOLONNE_TELEFON, telefon);
        values.put(DatabaseHjelper.KOLONNE_BURSDAG, bursdag);
        long insertId = database.insert(DatabaseHjelper.TABELL_VENNER, null,
                values);
        Cursor cursor = database.query(DatabaseHjelper.TABELL_VENNER, null,
                DatabaseHjelper.KOLONNE_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Venn nyVenn = cursorTilVenn(cursor);
        cursor.close();
        return nyVenn;
    }
    private Venn cursorTilVenn(Cursor cursor) {
        Venn venn = new Venn();
        venn.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_ID)));
        venn.setNavn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_NAVN)));
        venn.setTelefon(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_TELEFON)));
        venn.setBursdag(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHjelper.KOLONNE_BURSDAG)));
        return venn;
    }

    public void slettVenn(long vennId) {
        database.delete(DatabaseHjelper.TABELL_VENNER, DatabaseHjelper.KOLONNE_ID + "=?", new String[]{Long.toString(vennId)});
    }
}
