package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

public class Sesja {
    private static Uzytkownik aktualnyUzytkownik;

    public static void ustawAktualnegoUzytkownika(Uzytkownik uzytkownik) {
        aktualnyUzytkownik = uzytkownik;
    }

    public static Uzytkownik getAktualnyUzytkownik() {
        return aktualnyUzytkownik;
    }

    public static void wyloguj() {
        aktualnyUzytkownik = null;
    }

    public static boolean czyZalogowany() {
        return aktualnyUzytkownik != null;
    }
}
