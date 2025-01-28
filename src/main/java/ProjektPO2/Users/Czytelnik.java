package ProjektPO2.Users;

import ProjektPO2.Rola;

public class Czytelnik extends Uzytkownik {
    public Czytelnik(String imie, String nazwisko, String nrKarty, String haslo) {
        super(imie, nazwisko, nrKarty, haslo, Rola.CZYTELNIK);
    }
}