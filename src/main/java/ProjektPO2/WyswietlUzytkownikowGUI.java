package ProjektPO2;

import ProjektPO2.Users.Uzytkownik;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class WyswietlUzytkownikowGUI {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = screenSize.width;
    private int screenHight = screenSize.height;
    private JPanel panel1;

    void wyswietlUzytkownikowGUI(Biblioteka biblioteka) {
        JFrame ksiazkiFrame = new JFrame("Lista Użytkowników");
        ksiazkiFrame.setSize(800, 600);
        ksiazkiFrame.setLocation(screenWidth/2-280, screenHight/2-180);

        String[] columnNames = {"Nr Karty", "Imię", "Nazwisko", "Zarezerwowane książki", "Wypożyczone książki"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Uzytkownik u : biblioteka.getUzytkownicy()) {
            StringBuilder borrowedBooksTitles = new StringBuilder();
            for (Ksiazka ksiazka : u.getWypozyczoneKsiazki()) {
                if (borrowedBooksTitles.length() > 0) {
                    borrowedBooksTitles.append(", ");
                }
                borrowedBooksTitles.append(ksiazka.getTytul());
            }

            StringBuilder reservedBooksTitles = new StringBuilder();
            for (Ksiazka ksiazka : u.getZarezerwowaneKsiazki()) {
                if (reservedBooksTitles.length() > 0) {
                    reservedBooksTitles.append(", ");
                }
                reservedBooksTitles.append(ksiazka.getTytul());
            }

            tableModel.addRow(new Object[]{
                    u.getNrKarty(),
                    u.getImie(),
                    u.getNazwisko(),
                    reservedBooksTitles.toString(),
                    borrowedBooksTitles.toString()
            });
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ksiazkiFrame.add(scrollPane);
        ksiazkiFrame.setVisible(true);
    }}