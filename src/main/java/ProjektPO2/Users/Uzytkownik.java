package ProjektPO2.Users;

import ProjektPO2.Ksiazka;
import ProjektPO2.Rola;

import java.util.ArrayList;
import java.io.Serializable;

public class Uzytkownik implements Serializable{
    private String imie;
    private String nazwisko;
    private String nrKarty;
    private String haslo;
    private Rola rola;
    private CustomArrayList<String> wypozyczoneKsiazki = new CustomArrayList<>();
    private CustomArrayList<String> zarezerwowaneKsiazki = new CustomArrayList<>();

    public Uzytkownik() {
        this.imie = "Nieznane";
        this.nazwisko = "Nieznane";
        this.nrKarty = "0000";
        this.haslo = "brak";
        this.rola = Rola.CZYTELNIK;
    }

    public Uzytkownik(String nrKarty){
        this.nrKarty = nrKarty;
        
    }

    public Uzytkownik(String[] daneZServera){
        this.imie = daneZServera[0];
        this.nazwisko = daneZServera[1];
        this.nrKarty = daneZServera[2];
        this.rola = Rola.valueOf(daneZServera[3]);
        this.wypozyczoneKsiazki = new CustomArrayList<>();
        this.zarezerwowaneKsiazki = new CustomArrayList<>();
    }

    public Uzytkownik(String imie, String nazwisko, String nrKarty, String haslo,Rola rola){
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.nrKarty = nrKarty;
        this.haslo = haslo;
        this.rola = rola;
        this.wypozyczoneKsiazki = new CustomArrayList<>();
        this.zarezerwowaneKsiazki = new CustomArrayList<>();
    }

    public Uzytkownik(Uzytkownik uzytkownik) {
        this.imie = uzytkownik.getImie();
        this.nazwisko = uzytkownik.getNazwisko();
        this.nrKarty = uzytkownik.getNrKarty();
        this.haslo = uzytkownik.getHaslo();
        this.rola = uzytkownik.getRola();
        this.wypozyczoneKsiazki = uzytkownik.wypozyczoneKsiazki;
        this.zarezerwowaneKsiazki = uzytkownik.zarezerwowaneKsiazki;
    }

    public String getImie() { return imie; }
    public String getNazwisko() { return nazwisko; }
    public String getNrKarty() { return nrKarty; }
    public String getHaslo() { return haslo; }
    public Rola getRola() { return rola; }
    public CustomArrayList<String> getWypozyczoneKsiazki() { return wypozyczoneKsiazki; }
    public CustomArrayList<String> getZarezerwowaneKsiazki() { return zarezerwowaneKsiazki; }
    public void setRola(Rola rola) {
        this.rola = rola;
    }

    public void wypozyczKsiazke(String tytul){
        wypozyczoneKsiazki.add(tytul);
    }

    public void zarezerwujKsiazke(String tytul) {
        zarezerwowaneKsiazki.add(tytul);
    }

    @Override
    public String toString(){
        return "Użytkownik{" +
                "Imie: " + imie + '\'' +
                ", Nazwisko: " + nazwisko + '\'' +
                ", nrKarty: " + nrKarty + '\'' +
                ", Rola: " + rola + '\'' +
                ", wypozyczoneKsiazki: " + wypozyczoneKsiazki + '\'' +
                ", zarezerwowaneKsiazki: " + zarezerwowaneKsiazki + "}";
    }

    public String toStringServer(){
        return  imie +
                "#" + nazwisko +
                "#" + nrKarty +
                "#" + rola +
                "#" + wypozyczoneKsiazki +
                "#" + zarezerwowaneKsiazki;
    }

}
