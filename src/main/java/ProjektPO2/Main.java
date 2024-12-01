package ProjektPO2;

import java.util.Scanner;
import java.util.Date;

public class Main {
    public static void main(String[] args){
        Biblioteka biblioteka = new Biblioteka();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while(running) {
            System.out.println("\n ==== Biblioteka ====");
            System.out.println("0. Koniec");
            System.out.println("1. Dodaj użytkownika");
            System.out.println("2. Dodaj książkę");
            System.out.println("3. Wypożycz książkę");
            System.out.println("4. Zwróć książkę");
            System.out.println("5. Zarezerwuj książkę");
            System.out.println("6. Wyświetl użytkowników");
            System.out.println("7. Wyświetl książki");
            System.out.println("8. Zapisz do pliku");
            System.out.println("9. Wczytaj z pliku");

            int opcja = scanner.nextInt();
            scanner.nextLine();

            switch (opcja) {
                case 0:
                    running = false;
                    System.out.println("Zamykanie aplikacji...");
                    break;
                case 1:
                    System.out.println("Podaj imie: ");
                    String imie = scanner.nextLine();
                    System.out.println("Podaj nazwisko: ");
                    String nazwisko = scanner.nextLine();
                    System.out.println("Podaj hasło: ");
                    String haslo = scanner.nextLine();
                    biblioteka.dodajUzytkownika(imie, nazwisko, haslo);
                    System.out.println("Dodano uzytkownika: " + imie + " " + nazwisko);
                    break;
                case 2:
                    dodajKsiazke(biblioteka, scanner);
                    break;
                case 3:
                    wypozyczKsiazke(biblioteka, scanner);
                    break;
                case 4:
                    zwrocKsiazke(biblioteka, scanner);
                    break;
                case 5:
                    zarezerwujKsiazke(biblioteka, scanner);
                    break;
                case 6:
                    biblioteka.wyswietlUzytkownikow();
                    break;
                case 7:
                    biblioteka.wyswietlKsiazki();
                    break;
                case 8:
                    System.out.println("Podaj ścieżkę zapisu do pliku: ");
                    String sciezkaZapisu = scanner.nextLine();
                    biblioteka.zapiszDoPliku(sciezkaZapisu);
                    break;
                case 9:
                    System.out.println("Podaj ścieżkę pliku do wczytania: ");
                    String sciezkaWczytania = scanner.nextLine();
                    biblioteka.wczytajZPliku(sciezkaWczytania);
                    break;
            }
        }
    }

    private static void dodajKsiazke(Biblioteka biblioteka, Scanner scanner) {
        System.out.println("Podaj tytuł: ");
        String tytul = scanner.nextLine();
        System.out.println("Podaj autora: ");
        String autor = scanner.nextLine();
        System.out.println("Podaj kategorię książki: ");
        String kategoria = scanner.nextLine();
        System.out.println("Podaj ścieżkę do pliku okładki: ");
        String okladka = scanner.nextLine();

        Ksiazka ksiazka = new Ksiazka(tytul, autor, true, null, false, kategoria, okladka);
        biblioteka.dodajKsiazke(ksiazka);
        System.out.println("Dodano ksiązkę: " + ksiazka);
    }

    private static void wypozyczKsiazke(Biblioteka biblioteka, Scanner scanner) {
        System.out.println("Podaj numer karty: ");
        String nrKarty = scanner.nextLine();
        System.out.println("Podaj tytuł książki: ");
        String tytul = scanner.nextLine();

        boolean result = biblioteka.wypozyczKsiazke(nrKarty, tytul);
        if(result) {
            System.out.println("Wypozyczono książkę");
        } else {
            System.out.println("Nie udało się wypożyczyć książki");
        }
    }

    private static void zwrocKsiazke(Biblioteka biblioteka, Scanner scanner) {
        System.out.println("Podaj numer karty: ");
        String nrKarty = scanner.nextLine();
        System.out.println("Podaj tytuł książki: ");
        String tytul = scanner.nextLine();

        boolean result = biblioteka.zwrocKsiazke(nrKarty, tytul);
        if(result) {
            System.out.println("Zwrócono książkę");
        } else {
            System.out.println("Nie udało się zwrócić książki");
        }
    }

    private static void zarezerwujKsiazke(Biblioteka biblioteka, Scanner scanner) {
        System.out.println("Podaj numer karty: ");
        String nrKarty = scanner.nextLine();
        System.out.println("Podaj tytuł książki: ");
        String tytul = scanner.nextLine();

        boolean result = biblioteka.zarezerwujKsiazke(nrKarty, tytul);
        if(result) {
            System.out.println("Zarezerwowano książkę");
        } else {
            System.out.println("Nie udało się zrezerwować książki");
        }

    }

}
