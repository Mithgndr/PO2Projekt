package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import java.io.Serializable;

public class Ksiazka implements Serializable {
    private final String tytul;
    private final String autor;
    private boolean czyDostepna;
    private String niedostepnaDo;
    private boolean czyZarezerwowana;
    private final String kategoria;
    private Biblioteka biblioteka;
    private Uzytkownik uzytkownik;

    public Ksiazka(){
        this.tytul="";
        this.autor="";
        this.czyDostepna = false;
        this.niedostepnaDo = null;
        this.czyZarezerwowana = false;
        this.kategoria = "";
    }

    public Ksiazka(String tytul, String autor, boolean czyDostepna, String niedostepnaDo, boolean czyZarezerwowana, String kategoria) {
        this.tytul = tytul;
        this.autor = autor;
        this.czyDostepna = czyDostepna;
        this.niedostepnaDo = niedostepnaDo;
        this.czyZarezerwowana = czyZarezerwowana;
        this.kategoria = kategoria;

    }

    public Ksiazka(String[] daneZServera) {
        this.tytul = daneZServera[0];
        this.autor = daneZServera[1];
        this.czyDostepna = Boolean.parseBoolean(daneZServera[2]);
        this.niedostepnaDo = daneZServera[3];
        this.czyZarezerwowana = Boolean.parseBoolean(daneZServera[4]);
        this.kategoria = daneZServera[5];
    }


    public Ksiazka(Biblioteka biblioteka, String tytul) {
        this.biblioteka = biblioteka;
        this.tytul = tytul;
        this.autor = biblioteka.znajdzKsiazke(tytul).getAutor();
        this.czyDostepna = biblioteka.znajdzKsiazke(tytul).getCzydostepna();
        this.niedostepnaDo = biblioteka.znajdzKsiazke(tytul).getNiedostepnaDo();
        this.czyZarezerwowana = biblioteka.znajdzKsiazke(tytul).getCzyZarezerwowana();
        this.kategoria = biblioteka.znajdzKsiazke(tytul).getKategoria();
    }

    public String getTytul() {return tytul;}
    public String getAutor() {return autor;}
    public boolean getCzydostepna() {return czyDostepna;}
    public String getNiedostepnaDo() { return niedostepnaDo; }
    public boolean getCzyZarezerwowana() {return czyZarezerwowana;}
    public String getKategoria() {return kategoria;}

    public void ustawDostepnosc(boolean dostepna, String dostepnaOd, boolean czyZarezerwowana) {
        this.czyDostepna = dostepna;
        this.niedostepnaDo = dostepnaOd;
        this.czyZarezerwowana = czyZarezerwowana;
    }


    @Override
    public String toString(){
        return  tytul +
                "$" + autor +
                "$" + czyDostepna +
                "$" + niedostepnaDo +
                "$" + czyZarezerwowana +
                "$" + kategoria;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ksiazka ksiazka = (Ksiazka) obj;
        return tytul.equals(ksiazka.tytul);
    }

    @Override
    public int hashCode() {
        return tytul.hashCode();
    }

    public void setCzyZarezerwowana(boolean b) {
        this.czyZarezerwowana = b;
    }

    public void setCzyDostepna(boolean b) {
        this.czyDostepna = b;
    }

    public Object getUzytkownik() {
        uzytkownik = biblioteka.znajdzUzytkownika(tytul);
        return uzytkownik;
    }

    public void setUzytkownik(String currentUser) {
        this.uzytkownik = biblioteka.znajdzUzytkownika(currentUser);
    }
}
