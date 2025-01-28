package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final Biblioteka biblioteka;
    private final Uzytkownik uzytkownik;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int screenWidth = screenSize.width;
    private final int screenHight = screenSize.height;
    private final PrintWriter out;

    private static final String SERVER_ADDRESS = "localhost"; // Możesz zmienić na adres IP serwera
    private static final int SERVER_PORT = 23456;


    public BibliotekaGUI(Biblioteka biblioteka, Uzytkownik uzytkownik) throws IOException {
        this.uzytkownik = uzytkownik;
        this.biblioteka = biblioteka;
        Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        this.out = new PrintWriter(socket.getOutputStream(), true);

        initGUI();
    }


    private void initGUI() {
        JFrame frame = new JFrame("System Zarządzania Biblioteką - Bibliotekarz");
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
                frame.dispose();
                dodajUzytkownikaDialog();
                try {
                    new BibliotekaGUI(biblioteka, uzytkownik);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnUsunUzytkownika.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                usunUzytkownikaDialog();
                try {
                    new BibliotekaGUI(biblioteka, uzytkownik);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnDodajKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                dodajKsiazkeDialog();
                try {
                    new BibliotekaGUI(biblioteka, uzytkownik);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                new WyswietlUzytkownikowGUI().wyswietlUzytkownikowGUI(biblioteka);
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
            biblioteka.dodajUzytkownika(imie, nazwisko, haslo, rola);
            Uzytkownik nowyUzytkownik = biblioteka.getUzytkownicy().getLast();
            JOptionPane.showMessageDialog(null, "Dodano użytkownika!");
            System.out.print("Dodano uzytkownika " + imie +" "+ nazwisko + " z rolą: " + rola + "\n");
            out.println("ADD_USER;" + nowyUzytkownik.getImie() + ";" + nowyUzytkownik.getNazwisko() + ";" + nowyUzytkownik.getNrKarty() + ";" + nowyUzytkownik.getHaslo() + ";" + nowyUzytkownik.getRola());
        }
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
                out.println("REMOVE;" + nrKarty);
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Nie udało się usunąć użytkownika!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
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
            out.println("ADD_BOOK;" + ksiazka);
        }
    }
}