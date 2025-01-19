package ProjektPO2.Users;

import ProjektPO2.Rola;

public class Bibliotekarz extends Uzytkownik {
    public Bibliotekarz(String imie, String nazwisko, String nrKarty, String haslo) {
        super(imie, nazwisko, nrKarty, haslo, Rola.BIBLIOTEKARZ);
    }
}