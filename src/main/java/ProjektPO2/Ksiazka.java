package ProjektPO2;

import java.io.Serializable;
import java.util.Date;

public class Ksiazka implements Serializable {
    private final String tytul;
    private final String autor;
    private boolean czyDostepna;
    private Date niedostepnaDo;
    private boolean czyZarezerwowana;
    private final String kategoria;
    private final String okladka;

    public Ksiazka(String tytul, String autor, boolean czyDostepna, Date niedostepnaDo, boolean czyZarezerwowana, String kategoria, String okladka) {
        this.tytul = tytul;
        this.autor = autor;
        this.czyDostepna = czyDostepna;
        this.niedostepnaDo = niedostepnaDo;
        this.czyZarezerwowana = czyZarezerwowana;
        this.kategoria = kategoria;
        this.okladka = okladka;
    }

    public String getTytul() {return tytul;}
    public String getAutor() {return autor;}
    public boolean getCzydostepna() {return czyDostepna;}
    public Date getNiedostepnaDo() { return niedostepnaDo; }
    public boolean getCzyZarezerwowana() {return czyZarezerwowana;}
    public String getKategoria() {return kategoria;}
    public String getOkladka() {return okladka;}

    public void ustawDostepnosc(boolean dostepna, Date dostepnaOd, boolean czyZarezerwowana) {
        this.czyDostepna = dostepna;
        this.niedostepnaDo = dostepnaOd;
        this.czyZarezerwowana = czyZarezerwowana;
    }

    @Override
    public String toString(){
        return "Książka{" +
                "tytuł:  "+ tytul + '\'' +
                ", autor: " + autor + '\'' +
                ", czyDostępna: " + czyDostepna + '\'' +
                ", niedostepnaDo: " + niedostepnaDo + '\'' +
                ", czyZarezerwowana: " + czyZarezerwowana + '\'' +
                ", kategoria: " + kategoria + '\'' +
                ", okladka: " + okladka + '\'' + '}';

    }
}
