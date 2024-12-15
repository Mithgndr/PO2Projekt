package ProjektPO2;

import java.util.ArrayList;
import java.io.Serializable;

public class Uzytkownik implements Serializable{
    private String imie;
    private String nazwisko;
    private String nrKarty;
    private String haslo;
    private ArrayList<Ksiazka> wypozyczoneKsiazki;
    private ArrayList<Ksiazka> zarezerwowaneKsiazki;

    public Uzytkownik(String imie, String nazwisko, String nrKarty, String haslo){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrKarty = nrKarty;
        this.haslo = haslo;
        this.wypozyczoneKsiazki = new ArrayList<>();
        this.zarezerwowaneKsiazki = new ArrayList<>();
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getNrKarty() { return nrKarty; }
    public String getHaslo() { return haslo; }
    public ArrayList<Ksiazka> getWypozyczoneKsiazki() { return wypozyczoneKsiazki; }
    public ArrayList<Ksiazka> getZarezerwowaneKsiazki() { return zarezerwowaneKsiazki; }


    public void wypozyczKsiazke(Ksiazka ksiazka){
        wypozyczoneKsiazki.add(ksiazka);
    }

    @Override
    public String toString(){
        return "Użytkownik{" +
                "Imie: " + imie + '\'' +
                ", Nazwisko: " + nazwisko + '\'' +
                ", nrKarty: " + nrKarty + '\'' +
                ", wypozyczoneKsiazki: " + wypozyczoneKsiazki + '\'' +
                ", zarezerwowaneKsiazki: " + zarezerwowaneKsiazki + "}";
    }

}
