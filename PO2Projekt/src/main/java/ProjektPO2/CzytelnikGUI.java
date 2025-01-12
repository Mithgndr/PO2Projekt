package ProjektPO2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CzytelnikGUI {

    private JPanel panel1;
    private JButton btcWyswietlDostepneKsiazki;
    private JButton btcZarezerwujKsiazke;
    private JButton btcOddajKsiazke;
    private JLabel ekran;
    private Biblioteka biblioteka = new Biblioteka();
    private Uzytkownik uzytkownik;
    private Ksiazka ksiazka = new Ksiazka();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHeight = screenSize.height;


    public CzytelnikGUI(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
        biblioteka.wczytajZPliku("dane.json");
        initGUI();
    }

    private void initGUI() {
        JFrame frame = new JFrame("System Zarządzania Biblioteką - Czytelnik");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocation(screenWidth / 2 - 300, screenHeight / 2 - 200);


        frame.add(panel1);
        frame.setVisible(true);

        btcWyswietlDostepneKsiazki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WyswietlKsiazkiGUI wyswietlKsiazkiGUI = new WyswietlKsiazkiGUI();
                wyswietlKsiazkiGUI.wyswietlKsiazkiGUI(biblioteka);
            }
        });

        btcZarezerwujKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zarezerwujKsiazkeDialog();
            }
        });

        btcOddajKsiazke.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zwrocKsiazkeDialog();
            }
        });
    }


    private void zarezerwujKsiazkeDialog() {
        JTextField tytulField = new JTextField();

        Object[] fields = {
                "Tytuł książki do rezerwacji:", tytulField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Zarezerwuj książkę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String tytul = tytulField.getText();
            String nrKarty = uzytkownik.getNrKarty();
            boolean success = biblioteka.zarezerwujKsiazke(nrKarty,tytul);
            if (nrKarty != null && !nrKarty.isEmpty()) {
                if (success) {
                    JOptionPane.showMessageDialog(null, "Książka została zarezerwowana!");
                } else {
                    JOptionPane.showMessageDialog(null, "Nie udało się zarezerwować książki.");
                }
                biblioteka.zapiszDoPliku("dane.json");
            } else {
                JOptionPane.showMessageDialog(null, "Błąd: brak numeru karty użytkownika.");
            }
            biblioteka.zapiszDoPliku("dane.json");
        }
    }

    private void zwrocKsiazkeDialog() {
        JTextField tytulField = new JTextField();

        Object[] fields = {
                "Tytuł książki do oddania:", tytulField
        };

        int option = JOptionPane.showConfirmDialog(null, fields, "Oddaj książkę", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String tytul = tytulField.getText();
            boolean success = biblioteka.zwrocKsiazke(uzytkownik.getNrKarty(), ksiazka.getTytul());
            if (success) {
                JOptionPane.showMessageDialog(null, "Książka została oddana!");
            } else {
                JOptionPane.showMessageDialog(null, "Nie udało się oddać książki.");
            }
            biblioteka.zapiszDoPliku("dane.json");
        }
    }

}
