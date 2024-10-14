package com.example.oblig2s375045s375063;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHjelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAVN = "venner.db";
    private static final int DATABASE_VERSJON = 1;

    public static final String TABELL_VENNER = "venner";
    public static final String KOLONNE_ID = "id";
    public static final String KOLONNE_NAVN = "navn";
    public static final String KOLONNE_TELEFON = "telefon";
    public static final String KOLONNE_BURSDAG = "bursdag";

    private static final String CREATE_TABLE_VENNER = "CREATE TABLE " + TABELL_VENNER + "("
            + KOLONNE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KOLONNE_NAVN + " TEXT NOT NULL,"
            + KOLONNE_TELEFON + " TEXT NOT NULL,"
            + KOLONNE_BURSDAG + " TEXT NOT NULL)";

        public DatabaseHjelper(Context context) {
        super(context, DATABASE_NAVN, null, DATABASE_VERSJON);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_VENNER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int gammelVersjon, int nyVersjon) {
            db.execSQL("DROP TABLE IF EXISTS " + TABELL_VENNER);
            onCreate(db);
    }
}

