package ProjektPO2;

import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class Biblioteka {
    private ArrayList<Uzytkownik> uzytkownicy;
    private ArrayList<Ksiazka> ksiazki;
    private static int nextNumerKarty = 1000;

    public Biblioteka() {
        this.uzytkownicy = new ArrayList<>();
        this.ksiazki = new ArrayList<>();
    }

    private String generujNrKarty() {
        return String.valueOf(nextNumerKarty++);
    }

    public void dodajUzytkownika(String imie, String nazwisko, String haslo) {
        String nrKarty = generujNrKarty();
        Uzytkownik uzytkownik = new Uzytkownik(imie, nazwisko, nrKarty, haslo);
        uzytkownicy.add(uzytkownik);
    }

    public void dodajKsiazke(Ksiazka ksiazka) {
        ksiazki.add(ksiazka);
    }

    public Uzytkownik znajdzUzytkownika(String nrKarty) {
        for(Uzytkownik u : uzytkownicy) {
            if(u.getNrKarty().equals(nrKarty)) {
                return u;
            }
        }
        return null;
    }

    public Ksiazka znajdzKsiazke(String tytul) {
        for(Ksiazka k : ksiazki) {
            if(k.getTytul().equals(tytul)) {
                return k;
            }
        }
        return null;
    }

    public boolean wypozyczKsiazke(String nrKarty, String tytul){
        Uzytkownik uzytkownik = znajdzUzytkownika(nrKarty);
        Ksiazka ksiazka =znajdzKsiazke(tytul);

        if(uzytkownik != null && ksiazka != null) {
            if(ksiazka.getCzydostepna()) {
                ksiazka.ustawDostepnosc(false, new Date(), true);
                uzytkownik.wypozyczKsiazke(ksiazka);
                System.out.println("Książka: " + tytul + "została wypożyczona przez użytkownika: " + nrKarty);
                return true;
            } else {
                System.out.println("Książka: " + tytul + "jest niedostępna");
                return false;
            }
        } else {
            System.out.println("Książka albo użytkownik nie został znaleziony");
            return false;
        }
    }

    public boolean zwrocKsiazke(String nrKarty, String tytul){
        Uzytkownik uzytkownik = znajdzUzytkownika(nrKarty);
        Ksiazka ksiazka =znajdzKsiazke(tytul);

        if(uzytkownik != null && ksiazka != null) {
            if(uzytkownik.getWypozyczoneKsiazki().contains(ksiazka)) {
                ksiazka.ustawDostepnosc(true, null, false);
                uzytkownik.getWypozyczoneKsiazki().remove(ksiazka);
                System.out.println("Książka: " + tytul + "została zwrócona");
                return true;
            } else {
                System.out.println("Uzytkownik nie posiada takiej książki");
                return false;
            }
        } else {
            System.out.println("Książka albo użytkownik nie został znaleziony");
            return false;
        }
    }

    public boolean zarezerwujKsiazke(String nrKarty, String tytul){
        Uzytkownik uzytkownik = znajdzUzytkownika(nrKarty);
        Ksiazka ksiazka =znajdzKsiazke(tytul);

        if(uzytkownik != null && ksiazka != null) {
            if(!ksiazka.getCzyZarezerwowana()) {
                ksiazka.ustawDostepnosc(true, null, true);
                uzytkownik.getWypozyczoneKsiazki().remove(ksiazka);
                uzytkownik.getZarezerwowaneKsiazki().add(ksiazka);
                System.out.println("Książka: " + tytul + "została zarezerwowana przez użytkownika: " + nrKarty);
                return true;
            } else {
                System.out.println("Książka: " + ksiazka + "jest zarezerwowana");
                return false;
            }
        } else {
            System.out.println("Książka albo użytkownik nie został znaleziony");
            return false;
        }
    }

    public void wyswietlUzytkownikow() {
        for(Uzytkownik u : uzytkownicy){
            System.out.println(u);
        }
    }

    public void wyswietlKsiazki() {
        for(Ksiazka k : ksiazki) {
            System.out.println(k);
        }
    }

    public void zapiszDoPliku(String sciezka) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(sciezka)) {
            BibliotekaData data = new BibliotekaData(uzytkownicy, ksiazki, nextNumerKarty);
            gson.toJson(data, writer);
            System.out.println("Dane zapisane do pliku JSON: " + sciezka);
        } catch (IOException e) {
            System.out.println("Błąd podczas zapisywania: " + e.getMessage());
        }
    }

    public void wczytajZPliku(String sciezka) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(sciezka)) {
            BibliotekaData data = gson.fromJson(reader, BibliotekaData.class);
            this.uzytkownicy = data.getUzytkownicy();
            this.ksiazki = data.getKsiazki();
            nextNumerKarty = data.getNextNrKarty();
            System.out.println("Dane wczytane z pliku JSON: " + sciezka);
        } catch (IOException e) {
            System.out.println("Błąd podczas wczytywania: " + e.getMessage());
        }
    }

    public ArrayList<Ksiazka> getKsiazki() { return ksiazki; }
    public ArrayList<Uzytkownik> getUzytkownicy() { return uzytkownicy; }
    public static int getNextNumerKarty() { return nextNumerKarty; }

    public static class BibliotekaData {
        private final ArrayList<Uzytkownik> uzytkownicy;
        private final ArrayList<Ksiazka> ksiazki;
        private final int nextNrKarty;

        public BibliotekaData(ArrayList<Uzytkownik> uzytkownicy, ArrayList<Ksiazka> ksiazki, int nextNrKarty) {
            this.uzytkownicy = uzytkownicy;
            this.ksiazki = ksiazki;
            this.nextNrKarty = nextNrKarty;
        }

        public ArrayList<Uzytkownik> getUzytkownicy() {return uzytkownicy;}

        public ArrayList<Ksiazka> getKsiazki() {return ksiazki; }

        public int getNextNrKarty() { return nextNrKarty; }
    }
}
