package ProjektPO2;

import java.util.ArrayList;

public class Uzytkownik {
    private String imie;
    private String nazwisko;
    private String nrKarty;
    private ArrayList<Ksiazka> wypozyczoneKsiazki;

    public Uzytkownik(String imie, String nazwisko, String nrKarty){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrKarty = nrKarty;
        this.wypozyczoneKsiazki = new ArrayList<>();
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getNrKarty() { return nrKarty; }
    public ArrayList<Ksiazka> getWypozyczoneKsiazki() { return wypozyczoneKsiazki; }

    public void wypozyczKsiazke(Ksiazka ksiazka){
        wypozyczoneKsiazki.add(ksiazka);
    }

    @Override
    public String toString(){
        return "Użytkownik{" +
                "Imie: " + imie + '\'' +
                ", Nazwisko: " + nazwisko + '\'' +
                ", nrKarty: " + nrKarty + '\'' +
                ", wypozyczoneKsiazki: " + wypozyczoneKsiazki + '\'' + '}';
    }

}
