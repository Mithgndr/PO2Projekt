package ProjektPO2;

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
        ksiazkiFrame.setSize(600, 400);
        ksiazkiFrame.setLocation(screenWidth/2-280, screenHight/2-180);

        String[] columnNames = {"Nr Karty", "Imię", "Nazwisko", "Zarezerwowane książki", "Wypożyczone książki"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Uzytkownik u : biblioteka.getUzytkownicy()) {
            tableModel.addRow(new Object[]{u.getNrKarty(), u.getImie(), u.getNazwisko(), u.getZarezerwowaneKsiazki(), u.getWypozyczoneKsiazki()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        ksiazkiFrame.add(scrollPane);
        ksiazkiFrame.setVisible(true);
    }

}