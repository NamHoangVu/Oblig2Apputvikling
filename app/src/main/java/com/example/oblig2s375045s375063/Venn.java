package com.example.oblig2s375045s375063;

public class Venn {
    private int id; // Unik ID for hver venn
    private String navn;
    private String telefon;
    private String bursdag;

    public Venn(int id, String navn, String telefon, String bursdag) {
        this.id = id;
        this.navn = navn;
        this.telefon = telefon;
        this.bursdag = bursdag;
    }

    // Gettere
    public int getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getBursdag() {
        return bursdag;
    }
}
