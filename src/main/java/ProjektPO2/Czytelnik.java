package ProjektPO2;

public class Czytelnik extends Uzytkownik {
    public Czytelnik(String imie, String nazwisko, String nrKarty, String haslo) {
        super(imie, nazwisko, nrKarty, haslo, Rola.CZYTELNIK);
    }
}