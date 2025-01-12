package ProjektPO2;

import java.io.Serializable;

public class Ksiazka implements Serializable {
    private final String tytul;
    private final String autor;
    private boolean czyDostepna;
    private boolean czyZarezerwowana;
    private final String kategoria;

    public Ksiazka(String ksiazka) {
        String[] s = ksiazka.split("#");
        this.tytul = s[0];
        this.autor = s[1];
        this.czyDostepna = Boolean.parseBoolean(s[2]);
        this.czyZarezerwowana = Boolean.parseBoolean(s[4]);
        this.kategoria = s[5];

    }

    public String getTytul() {return tytul;}
    public String getAutor() {return autor;}
    public boolean getCzydostepna() {return czyDostepna;}
    public boolean getCzyZarezerwowana() {return czyZarezerwowana;}
    public String getKategoria() {return kategoria;}

    public void ustawDostepnosc(boolean dostepna, boolean czyZarezerwowana) {
        this.czyDostepna = dostepna;
        this.czyZarezerwowana = czyZarezerwowana;
    }

    @Override
    public String toString(){
        return  "#" + tytul +
                "#" + autor +
                "#" + czyDostepna +
                "#" + czyZarezerwowana +
                "#" + kategoria;

    }
}
