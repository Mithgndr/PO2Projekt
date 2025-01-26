package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class BibliotekaGUI {

    private JPanel Panel1;
    private JButton btnDodajUzytkownika;
    private JButton btnUsunUzytkownika;
    private JButton btnDodajKsiazke;
    private JButton btnWyswietlKsiazki;
    private JButton btnWyswietlUzytkownikow;
    private Biblioteka biblioteka = new Biblioteka();
    private Uzytkownik uzytkownik;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;


    public BibliotekaGUI(Uzytkownik uzytkownik) {
        initGUI(uzytkownik);
    }


    private void initGUI(Uzytkownik uzytkownik) {
        JFrame frame = new JFrame("System Zarządzania Biblioteką");
        Font font = new Font("Arial", Font.PLAIN, 20);
        Font font_bold = new Font("Arial", Font.BOLD, 20);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth/2, screenHight/2);
        frame.setLocation(screenWidth/4, screenHight/4);

        frame.add(Panel1);
        frame.setFont(font);
        btnDodajUzytkownika.setFont(font_bold);
        btnDodajKsiazke.setFont(font_bold);
        btnWyswietlKsiazki.setFont(font_bold);
        btnWyswietlUzytkownikow.setFont(font_bold);
        btnUsunUzytkownika.setFont(font_bold);
        frame.setVisible(true);

        btnDodajUzytkownika.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajUzytkownikaDialog();
            }
        });
        btnUsunUzytkownika.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usunUzytkownikaDialog();
            }
        });

        btnDodajKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajKsiazkeDialog();
            }
        });

        btnWyswietlKsiazki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new WyswietlKsiazkiGUI(biblioteka, uzytkownik);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnWyswietlUzytkownikow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WyswietlUzytkownikowGUI wyswietlUzytkownikowGUI = new WyswietlUzytkownikowGUI();
                wyswietlUzytkownikowGUI.wyswietlUzytkownikowGUI(biblioteka);
            }
        });
    }

    private void dodajUzytkownikaDialog() {
        JTextField imieField = new JTextField();
        JTextField nazwiskoField = new JTextField();
        JTextField hasloField = new JTextField();

        String[] role = { "Czytelnik", "Bibliotekarz" };
        JComboBox<String> rolaComboBox = new JComboBox<>(role);

        Object[] fields = {
                "Imię:", imieField,
                "Nazwisko:", nazwiskoField,
                "Hasło:", hasloField,
                "Rola: ", rolaComboBox

        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Dodaj użytkownika", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String imie = imieField.getText();
            String nazwisko = nazwiskoField.getText();
            String haslo = hasloField.getText();
            String rolaStr = (String) rolaComboBox.getSelectedItem();
            if (imie.isEmpty() || nazwisko.isEmpty() || haslo.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Wszystkie pola muszą być wypełnione!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            Rola rola = (rolaStr.equals("Czytelnik")) ? Rola.CZYTELNIK : Rola.BIBLIOTEKARZ;
            biblioteka.dodajUzytkownika(imie, nazwisko, haslo,rola);
            JOptionPane.showMessageDialog(null, "Dodano użytkownika!");
            System.out.print("Dodano uzytkownika " + imie +" "+ nazwisko + " z rolą: " + rola + "\n");
        }
        biblioteka.zapiszDoPliku("dane.json");
    }
    private void usunUzytkownikaDialog() {
        JTextField nrKartyField = new JTextField();

        Object[] fields = {
                "Numer karty użytkownika:", nrKartyField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Usuń użytkownika", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nrKarty = nrKartyField.getText();
            if (nrKarty.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Pole numeru karty nie może być puste!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(biblioteka.usunUzytkownika(nrKarty)) {
                JOptionPane.showMessageDialog(null, "Usunieto użytkownika!");
                System.out.print("Usunieto uzytkownika" + nrKarty);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Nie udało się usunąć użytkownika!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        biblioteka.zapiszDoPliku("dane.json");
    }


    private void dodajKsiazkeDialog() {
        JTextField tytulField = new JTextField();
        JTextField autorField = new JTextField();
        JTextField kategoriaField = new JTextField();

        Object[] fields = {
                "Tytuł:", tytulField,
                "Autor:", autorField,
                "Kategoria:", kategoriaField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Dodaj książkę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String tytul = tytulField.getText();
            String autor = autorField.getText();
            String kategoria = kategoriaField.getText();
            Ksiazka ksiazka = new Ksiazka(tytul, autor, true, null, false, kategoria);
            biblioteka.dodajKsiazke(ksiazka);
            JOptionPane.showMessageDialog(null, "Dodano książkę!");
            biblioteka.zapiszDoPliku("dane.json");
        }
    }

    private void wypozyczKsiazkeDialog() {
        JTextField nrKartyField = new JTextField();
        JTextField tytulField = new JTextField();

        Object[] fields = {
                "Numer karty użytkownika:", nrKartyField,
                "Tytuł książki:", tytulField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Wypożycz książkę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nrKarty = nrKartyField.getText();
            String tytul = tytulField.getText();
            boolean success = biblioteka.wypozyczKsiazke(nrKarty, tytul);
            if (success) {
                JOptionPane.showMessageDialog(null, "Książka wypożyczona!");
            } else {
                JOptionPane.showMessageDialog(null, "Nie udało się wypożyczyć książki.");
            }
            biblioteka.zapiszDoPliku("dane.json");
        }
    }

    private void zwrocKsiazkeDialog() {
        JTextField nrKartyField = new JTextField();
        JTextField tytulField = new JTextField();

        Object[] fields = {
                "Numer karty użytkownika:", nrKartyField,
                "Tytuł książki:", tytulField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Zwróć książkę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nrKarty = nrKartyField.getText();
            String tytul = tytulField.getText();
            boolean success = biblioteka.zwrocKsiazke(nrKarty, tytul);
            if (success) {
                JOptionPane.showMessageDialog(null, "Książka zwrócona!");
            } else {
                JOptionPane.showMessageDialog(null, "Nie udało się zwrócić książki.");
            }
            biblioteka.zapiszDoPliku("dane.json");
        }
    }
}