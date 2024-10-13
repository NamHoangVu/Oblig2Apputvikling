package com.example.oblig2s375045s375063;

public class Venn {
    private long id; // Unik ID for hver venn
    private String navn;
    private String telefon;
    private String bursdag;

    public Venn() {
    }

    // Gettere
    public long getId() {
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

    // Settere
    public void setId(long id) {
        this.id = id;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setBursdag(String bursdag) {
        this.bursdag = bursdag;
    }
}
