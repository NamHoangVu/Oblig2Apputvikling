package com.example.oblig2s375045s375063;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
    public void slettVenn(long vennId) {
        database.delete(DatabaseHjelper.TABELL_VENNER, DatabaseHjelper.KOLONNE_ID + "=?", new String[]{Long.toString(vennId)});
    }
}
