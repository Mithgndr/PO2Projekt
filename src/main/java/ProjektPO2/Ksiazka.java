package ProjektPO2;

import java.util.Date;

public class Ksiazka {
    private String tytul;
    private String autor;
    private boolean czyDostepna;
    private Date niedostepnaDo;
    private boolean czyZarezerwowana;
    private String opis;
    private String okladka;

    public Ksiazka(String tytul, String autor, boolean czyDostepna, Date niedostepnaDo, boolean czyZarezerwowana, String opis, String okladka) {
        this.tytul = tytul;
        this.autor = autor;
        this.czyDostepna = czyDostepna;
        this.niedostepnaDo = niedostepnaDo;
        this.czyZarezerwowana = czyZarezerwowana;
        this.opis = opis;
        this.okladka = okladka;
    }

    public String getTytul() {return tytul;}
    public String getAutor() {return autor;}
    public boolean getCzydostepna() {return czyDostepna;}
    public Date getNiedostepnaDo() { return niedostepnaDo; }
    public boolean getCzyZarezerwowana() {return czyZarezerwowana;}
    public String getOpis() {return tytul;}
    public String getOkladka() {return okladka;}

    public void ustawDostepnosc(boolean dostepna, Date dostepnaOd) {
        this.czyDostepna = dostepna;
        this.niedostepnaDo = dostepnaOd;
    }

    @Override
    public String toString(){
        return "Książka{" +
                "tytuł:  "+ tytul + '\'' +
                ", autor: " + autor + '\'' +
                ", czyDostępna: " + czyDostepna + '\'' +
                ", niedostepnaDo: " + niedostepnaDo + '\'' +
                ", czyZarezerwowana: " + czyZarezerwowana + '\'' +
                ", opis: " + opis + '\'' +
                ", okladka: " + okladka + '\'' + '}';

    }
}
