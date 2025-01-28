package ProjektPO2;

import ProjektPO2.Users.CustomArrayList;
import ProjektPO2.Users.Uzytkownik;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.List;

public class BibliotekaServer {

    private static final int PORT = 23456;
    private static CustomArrayList<String> ksiazki = new CustomArrayList<>(); // Prosta baza książek

    public static void main(String[] args) {
        System.out.println("Uruchamianie serwera biblioteki...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serwer uruchomiony na porcie " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nowe połączenie od: " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Problem z uruchomieniem serwera: " + e.getMessage());
        }
    }

    // Wewnętrzna klasa do obsługi klienta
    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Logowanie i przypisanie sesji do użytkownika
                String clientRequest = in.readLine();
                new Uzytkownik();
                Uzytkownik uzytkownik;
                Biblioteka biblioteka = new Biblioteka();
                biblioteka.wczytajZPliku("dane.json");


                if (clientRequest.startsWith("LOGIN")) {
                    String[] parts = clientRequest.split(";");
                    String login = parts[1];
                    String haslo = parts[2];

                    uzytkownik = biblioteka.znajdzUzytkownika(login);

                    if (uzytkownik != null && uzytkownik.getHaslo().equals(haslo)) {
                        out.println("SUCCESS;" + uzytkownik.getRola().toString());
                        wyslijKsiazki(biblioteka, out);
                        if(uzytkownik.getRola().equals(Rola.CZYTELNIK))
                            wyslijUzytkownika(uzytkownik, out);
                        else if(uzytkownik.getRola().equals(Rola.BIBLIOTEKARZ))
                            wyslijUzytkownikow(biblioteka, out);
                    } else {
                        out.println("FAILURE");
                    }
                }

                if (clientRequest.startsWith("BORROW")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String nrKarty = parts[1];
                    String tytul = parts[2];

                    uzytkownik = biblioteka.znajdzUzytkownika(nrKarty);
                    if(uzytkownik != null && biblioteka.znajdzKsiazke(tytul) != null) {
                        biblioteka.wypozyczKsiazke(nrKarty, tytul);
                        biblioteka.zapiszDoPliku("dane.json");
                        out.println("SUCCESS");
                        wyslijUzytkownika(uzytkownik, out);
                    } else {
                        out.println("FAILURE");
                    }
                }

                if (clientRequest.startsWith("RESERVE")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String nrKarty = parts[1];
                    String tytul = parts[2];

                    uzytkownik = biblioteka.znajdzUzytkownika(nrKarty);
                    if (uzytkownik != null && biblioteka.znajdzKsiazke(tytul) != null) {
                        biblioteka.zarezerwujKsiazke(nrKarty, tytul);
                        biblioteka.zapiszDoPliku("dane.json");
                        out.println("SUCCESS");
                        wyslijKsiazki(biblioteka, out);
                    } else {
                        out.println("FAILURE");
                    }
                }

                if (clientRequest.startsWith("RETURN")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String nrKarty = parts[1];
                    String tytul = parts[2];

                    uzytkownik = biblioteka.znajdzUzytkownika(nrKarty);
                    if (uzytkownik != null && biblioteka.znajdzKsiazke(tytul) != null) {
                        biblioteka.zwrocKsiazke(nrKarty, tytul);
                        biblioteka.zapiszDoPliku("dane.json");
                        out.println("SUCCESS");
                        wyslijUzytkownika(uzytkownik, out);
                    } else {
                        out.println("FAILURE");
                    }
                }

                if (clientRequest.startsWith("CANCEL")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String nrKarty = parts[1];
                    String tytul = parts[2];

                    uzytkownik = biblioteka.znajdzUzytkownika(nrKarty);
                    if (uzytkownik != null && biblioteka.znajdzKsiazke(tytul) != null) {
                        biblioteka.anulujRezerwacjeKsiazki(nrKarty, tytul);
                        biblioteka.zapiszDoPliku("dane.json");
                        out.println("SUCCESS");
                        wyslijUzytkownika(uzytkownik, out);
                    } else {
                        out.println("FAILURE");
                    }
                }

                if (clientRequest.startsWith("ADD_USER")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String imie = parts[1];
                    String nazwisko = parts[2];
                    String nrKarty = parts[3];
                    String haslo = parts[4];
                    Rola rola = Rola.valueOf(parts[5]);
                    biblioteka.dodajUzytkownika(imie, nazwisko, nrKarty, haslo, rola);
                    Biblioteka.setNextNumerKarty(Biblioteka.getNextNumerKarty() + 1);
                    biblioteka.zapiszDoPliku("dane.json");
                }

                if (clientRequest.startsWith("REMOVE")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String nrKarty = parts[1];
                    biblioteka.usunUzytkownika(nrKarty);
                    biblioteka.zapiszDoPliku("dane.json");
                }

                if (clientRequest.startsWith("ADD_BOOk")) {
                    System.out.println(clientRequest);
                    String[] parts = clientRequest.split(";");
                    String[] ksiazkaData = parts[1].split("\\$");
                    Ksiazka ksiazka = new Ksiazka(ksiazkaData);
                    biblioteka.dodajKsiazke(ksiazka);
                    biblioteka.zapiszDoPliku("dane.json");
                }
            } catch (IOException e) {
                System.err.println("Błąd połączenia z klientem: " + e.getMessage());
            }
        }


        void wyslijKsiazki(Biblioteka biblioteka, PrintWriter out) {
            try {
                for (Ksiazka ksiazka : biblioteka.getKsiazki()) {
                    System.out.println(out);
                    out.println(ksiazka.toString());
                }
                out.println("END");
            } catch (Exception e) {
                System.err.println("Błąd podczas obsługi żądania SHOW_BOOKS: " + e.getMessage());
                out.println("ERROR: Nie można wczytać książek.");
            }
        }

        void wyslijUzytkownika(Uzytkownik uzytkownik, PrintWriter out) {
            out.println("START_USER~" + uzytkownik.toStringServer());
        }

        void wyslijUzytkownikow(Biblioteka biblioteka, PrintWriter out) {
            CustomArrayList<Uzytkownik> uzytkownicy = biblioteka.getUzytkownicy();
            int i = 0;
            out.println("START_USERS");
            while (i < uzytkownicy.size()) {
                System.out.println(uzytkownicy.get(i).toStringServer());
                out.println(uzytkownicy.get(i).toStringServer());
                i++;
            }
            out.println("END_USERS");
            out.println(Biblioteka.getNextNumerKarty());
            System.out.println(Biblioteka.getNextNumerKarty());
        }
    }




}
