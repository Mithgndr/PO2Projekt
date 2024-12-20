package ProjektPO2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;

public class BibliotekaGUI {

    private JPanel Panel1;
    private JButton btnDodajUzytkownika;
    private JButton btnDodajKsiazke;
    private JButton btnWypozyczKsiazke;
    private JButton btnWyswietlKsiazki;
    private JButton btnZwrocKsiazke;
    private JButton btnWyswietlUzytkownikow;
    private Biblioteka biblioteka = new Biblioteka();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;

    public BibliotekaGUI() {
        biblioteka.wczytajZPliku("dane.json");
        initGUI();
    }


    private void initGUI() {
        JFrame frame = new JFrame("System Zarządzania Biblioteką");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocation(screenWidth/2 - 300, screenHight/2 - 200);

        frame.add(Panel1);
        frame.setVisible(true);

        btnDodajUzytkownika.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajUzytkownikaDialog();
            }
        });

        btnDodajKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dodajKsiazkeDialog();
            }
        });

        btnWypozyczKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wypozyczKsiazkeDialog();
            }
        });

        btnZwrocKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zwrocKsiazkeDialog();
            }
        });

        btnWyswietlKsiazki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WyswietlKsiazkiGUI wyswietlKsiazkiGUI = new WyswietlKsiazkiGUI();
                wyswietlKsiazkiGUI.wyswietlKsiazkiGUI(biblioteka);
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

        Object[] fields = {
                "Imię:", imieField,
                "Nazwisko:", nazwiskoField,
                "Hasło:", hasloField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Dodaj użytkownika", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String imie = imieField.getText();
            String nazwisko = nazwiskoField.getText();
            String haslo = hasloField.getText();
            biblioteka.dodajUzytkownika(imie, nazwisko, haslo);
            JOptionPane.showMessageDialog(null, "Dodano użytkownika!");
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
            Ksiazka ksiazka = new Ksiazka(tytul, autor, true, null, false, kategoria, null);
            biblioteka.dodajKsiazke(ksiazka);
            JOptionPane.showMessageDialog(null, "Dodano książkę!");
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
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BibliotekaGUI());
    }

}
