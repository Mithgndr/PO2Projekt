package ProjektPO2;

public class Bibliotekarz extends Uzytkownik {
    public Bibliotekarz(String imie, String nazwisko, String nrKarty, String haslo) {
        super(imie, nazwisko, nrKarty, haslo, Rola.BIBLIOTEKARZ);
    }
}